package com.tokopedia.mvc.presentation.creation.step1

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
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentVoucherCreationStepOneBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneAction
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneEvent
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneUiState
import com.tokopedia.mvc.presentation.creation.step2.VoucherCreationStepTwoActivity
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.mvc.util.constant.ImageUrlConstant
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class VoucherCreationStepOneFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance(
            pageMode: PageMode,
            voucherConfiguration: VoucherConfiguration
        ): VoucherCreationStepOneFragment {
            return VoucherCreationStepOneFragment().apply {
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
    private var binding by autoClearedNullable<SmvcFragmentVoucherCreationStepOneBinding>()

    //coachmark
    private val coachMark by lazy {
        context?.let {
            CoachMark2(it)
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(VoucherCreationStepOneViewModel::class.java) }

    private val pageMode by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val voucherConfiguration by lazy {
        arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration
            ?: VoucherConfiguration()
    }

    override fun getScreenName(): String =
        VoucherCreationStepOneFragment::class.java.canonicalName.orEmpty()

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
        binding = SmvcFragmentVoucherCreationStepOneBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.processEvent(
            VoucherCreationStepOneEvent.InitVoucherConfiguration(
                voucherConfiguration
            )
        )
        setVoucherType(voucherConfiguration.isVoucherProduct)
        setupView()
        observeUiState()
        observeUiAction()
        viewModel.processEvent(VoucherCreationStepOneEvent.HandleCoachmark)
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

    private fun setupView() {
        binding?.run {
            voucherTypeSelectionShop.apply {
                imgVoucherType?.setImageUrl(ImageUrlConstant.IMAGE_URL_SHOP_VOUCHER)
                radioButton?.setOnClickListener {
                    setVoucherType(false)
                }
            }
            voucherTypeSelectionProduct.apply {
                imgVoucherType?.setImageUrl(ImageUrlConstant.IMAGE_URL_PRODUCT_VOUCHER)
                radioButton?.setOnClickListener {
                    setVoucherType(true)
                }
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
        if (pageMode == PageMode.EDIT && isVoucherProduct != voucherConfiguration.isVoucherProduct) {
            showChangeVoucherTypeConfirmationDialog()
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

    private fun showChangeVoucherTypeConfirmationDialog() {
        // TODO: implement dialog
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

    private fun navigateToNextStep(pageMode: PageMode, voucherConfiguration: VoucherConfiguration) {
        if (pageMode == PageMode.CREATE) {
            VoucherCreationStepTwoActivity.start(requireContext(), voucherConfiguration)
            activity?.finish()
        } else {
            VoucherCreationStepTwoActivity.buildEditModeIntent(
                requireContext(),
                voucherConfiguration
            )
        }
    }
}
