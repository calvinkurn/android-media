package com.tokopedia.mvc.presentation.creation.step1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.utils.extension.disable
import com.tokopedia.campaign.utils.extension.enable
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentCreationVoucherTypeBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneAction
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneEvent
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneUiState
import com.tokopedia.mvc.presentation.creation.step2.VoucherInformationActivity
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.mvc.util.constant.ImageUrlConstant
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class VoucherTypeFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance(
            pageMode: PageMode,
            voucherConfiguration: VoucherConfiguration
        ): VoucherTypeFragment {
            return VoucherTypeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                    putParcelable(
                        BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION,
                        voucherConfiguration
                    )
                }
            }
        }
    }

    // binding
    private var binding by autoClearedNullable<SmvcFragmentCreationVoucherTypeBinding>()

    // coachmark
    private val coachMark by lazy {
        context?.let {
            CoachMark2(it)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(VoucherTypeViewModel::class.java) }

    private val pageMode by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val voucherConfiguration by lazy {
        arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration
            ?: VoucherConfiguration()
    }

    override fun getScreenName(): String =
        VoucherTypeFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerMerchantVoucherCreationComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentCreationVoucherTypeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.processEvent(
            VoucherCreationStepOneEvent.InitVoucherConfiguration(
                pageMode ?: PageMode.CREATE,
                voucherConfiguration
            )
        )
        setupView()
        observeUiState()
        observeUiAction()
        viewModel.processEvent(VoucherCreationStepOneEvent.HandleCoachmark)
        presetValue()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }

    private fun observeUiAction() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiAction.collect { event -> handleAction(event) }
        }
    }

    private fun handleUiState(state: VoucherCreationStepOneUiState) {
        renderHeader(state.voucherConfiguration.isVoucherProduct)
        renderLoading(state.isLoading)
        renderVoucherTypeSelection(state.voucherConfiguration.isVoucherProduct)
    }

    private fun handleAction(action: VoucherCreationStepOneAction) {
        when (action) {
            is VoucherCreationStepOneAction.ShowIneligibleState -> {
                showIneligibleState(action.isVoucherProduct)
            }
            is VoucherCreationStepOneAction.NavigateToNextStep -> {
                navigateToNextStep(action.pageMode, action.voucherConfiguration)
            }
            is VoucherCreationStepOneAction.ShowCoachmark -> {
                showCoachmark()
            }
            is VoucherCreationStepOneAction.ShowError -> {
                showError(action.error)
            }
        }
    }

    private fun presetValue() {
        viewModel.processEvent(
            VoucherCreationStepOneEvent.ChooseVoucherType(
                pageMode ?: PageMode.CREATE,
                voucherConfiguration.isVoucherProduct
            )
        )
    }

    private fun setupView() {
        binding?.run {
            voucherTypeSelectionShop.apply {
                imgVoucherType?.setImageUrl(ImageUrlConstant.IMAGE_URL_SHOP_VOUCHER)
                radioButton?.setOnClickListener { setVoucherType(false) }
                cardParent?.setOnClickListener { setVoucherType(false) }
            }
            voucherTypeSelectionProduct.apply {
                imgVoucherType?.setImageUrl(ImageUrlConstant.IMAGE_URL_PRODUCT_VOUCHER)
                radioButton?.setOnClickListener { setVoucherType(true) }
                cardParent?.setOnClickListener { setVoucherType(true) }
            }
        }
    }

    private fun renderHeader(isVoucherProduct: Boolean) {
        binding?.header?.apply {
            headerSubTitle = if (isVoucherProduct) {
                getString(R.string.smvc_creation_step_one_out_of_four_sub_title_label)
            } else {
                getString(R.string.smvc_creation_step_one_out_of_three_sub_title_label)
            }
            setNavigationOnClickListener { activity?.finish() }
        }
    }

    private fun renderLoading(isLoading: Boolean) {
        binding?.btnContinue?.isLoading = isLoading
    }

    private fun renderVoucherTypeSelection(isVoucherProduct: Boolean) {
        if (isVoucherProduct) setVoucherProductSelected() else setVoucherShopSelected()
        binding?.btnContinue?.apply {
            enable()
            setOnClickListener { viewModel.processEvent(VoucherCreationStepOneEvent.NavigateToNextStep) }
        }
    }

    /*
    * The proper ineligible state implementation
    * is still waiting update from the product team
    * this is just a dummy implementation
    */
    private fun showIneligibleState(isVoucherProduct: Boolean) {
        if (isVoucherProduct) setVoucherProductSelected() else setVoucherShopSelected()
        binding?.btnContinue?.apply {
            showToasterError(getString(R.string.smvc_ineligible_voucher_creation_error_text))
            disable()
        }
    }

    private fun setVoucherType(isVoucherProduct: Boolean) {
        if (voucherConfiguration.isFinishFilledStepOne) {
            showChangeVoucherTypeConfirmationDialog(isVoucherProduct)
        } else {
            viewModel.processEvent(
                VoucherCreationStepOneEvent.ChooseVoucherType(
                    pageMode ?: PageMode.CREATE,
                    isVoucherProduct
                )
            )
        }
    }

    private fun setVoucherShopSelected() {
        binding?.run {
            voucherTypeSelectionShop.isActive = true
            voucherTypeSelectionProduct.isActive = false
        }
    }

    private fun setVoucherProductSelected() {
        binding?.run {
            voucherTypeSelectionShop.isActive = false
            voucherTypeSelectionProduct.isActive = true
        }
    }

    private fun showChangeVoucherTypeConfirmationDialog(isVoucherProduct: Boolean) {
        val dialog = context?.let { ctx ->
            DialogUnify(
                ctx,
                DialogUnify.HORIZONTAL_ACTION,
                DialogUnify.NO_IMAGE
            )
        }
        dialog?.apply {
            setTitle(getString(R.string.smvc_change_voucher_type_confirmation_label))
            setDescription(getString(R.string.smvc_change_voucher_type_confirmation_description))
            setPrimaryCTAText(getString(R.string.smvc_change_voucher_type_confirmation_primary_cta_label))
            setSecondaryCTAText(getString(R.string.smvc_cancel))
            setPrimaryCTAClickListener {
                viewModel.processEvent(
                    VoucherCreationStepOneEvent.ChooseVoucherType(
                        pageMode ?: PageMode.CREATE,
                        isVoucherProduct
                    )
                )
                dismiss()
            }
            setSecondaryCTAClickListener { dismiss() }
        }?.show()
    }

    private fun showError(error: Throwable) {
        binding?.btnContinue.showToasterError(error.message ?: "")
    }

    private fun showCoachmark() {
        binding?.run {
            val coachMarkItem = ArrayList<CoachMark2Item>()
            coachMarkItem.add(
                CoachMark2Item(
                    voucherTypeSelectionProduct,
                    getString(R.string.smvc_voucher_creation_step_one_coachmark_title),
                    getString(R.string.smvc_voucher_creation_step_one_coachmark_description)
                )
            )
            coachMark?.showCoachMark(coachMarkItem)
            coachMark?.onDismissListener = { viewModel.setSharedPrefCoachMarkAlreadyShown() }
        }
    }

    private fun navigateToNextStep(
        pageMode: PageMode,
        currentVoucherConfiguration: VoucherConfiguration
    ) {
        if (pageMode == PageMode.CREATE) {
            navigateToVoucherInformationPage(pageMode, currentVoucherConfiguration)
        } else {
            navigateToNexStepInEditMode(pageMode, currentVoucherConfiguration)
        }
    }

    private fun navigateToNexStepInEditMode(
        pageMode: PageMode,
        currentVoucherConfiguration: VoucherConfiguration
    ) {
        if (currentVoucherConfiguration.isVoucherProduct != voucherConfiguration.isVoucherProduct) {
            navigateToVoucherInformationPage(pageMode, currentVoucherConfiguration)
        } else {
            navigateToVoucherSummaryPage(currentVoucherConfiguration)
        }
    }

    private fun navigateToVoucherInformationPage(
        pageMode: PageMode,
        currentVoucherConfiguration: VoucherConfiguration
    ) {
        if (pageMode == PageMode.CREATE) {
            context?.let { ctx -> VoucherInformationActivity.start(ctx, currentVoucherConfiguration) }
            activity?.finish()
        } else {
            context?.let { ctx -> VoucherInformationActivity.buildEditModeIntent(ctx, currentVoucherConfiguration) }
            activity?.finish()
        }
    }

    private fun navigateToVoucherSummaryPage(currentVoucherConfiguration: VoucherConfiguration) {
        val intent = Intent().apply {
            putExtra(
                BundleConstant.BUNDLE_KEY_VOUCHER_CREATION_STEP_ONE,
                currentVoucherConfiguration
            )
        }
        activity?.setResult(102, intent)
        activity?.finish()
    }
}
