package com.tokopedia.mvc.presentation.creation.step1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.tokopedia.mvc.R
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.utils.extension.disable
import com.tokopedia.campaign.utils.extension.enable
import com.tokopedia.campaign.utils.extension.showToasterError
import com.tokopedia.mvc.databinding.SmvcFragmentVoucherCreationStepOneBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationEvent
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneAction
import com.tokopedia.mvc.presentation.creation.step1.uimodel.VoucherCreationStepOneUiState
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class VoucherCreationStepOneFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance() = VoucherCreationStepOneFragment()
    }

    // binding
    private var binding by autoClearedNullable<SmvcFragmentVoucherCreationStepOneBinding>()

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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SmvcFragmentVoucherCreationStepOneBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        observeUiAction()
        observeUiState()
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
        renderToolbar(state.voucherConfiguration.isVoucherProduct)
        renderLoading(state.isLoading)
        renderVoucherTypeSelection(state.voucherConfiguration.isVoucherProduct)
    }

    private fun handleAction(event: VoucherCreationStepOneAction) {
        when (event) {
            is VoucherCreationStepOneAction.ShowIneligibleState -> {
                showIneligibleState(event.isVoucherProduct)
            }
            is VoucherCreationStepOneAction.ContinueToNextStep -> {
                //TODO: Navigate to step two
            }
            is VoucherCreationStepOneAction.ShowError -> {
                showError(event.error)
            }
        }
    }

    private fun setupView() {
        setVoucherType(voucherConfiguration.isVoucherProduct)
        binding?.run {
            voucherTypeSelectionShop.apply {
                radioButton?.setOnClickListener {
                    setVoucherType(false)
                }
            }
            voucherTypeSelectionProduct.apply {
                radioButton?.setOnClickListener {
                    setVoucherType(true)
                }
            }
        }
    }

    private fun renderToolbar(isVoucherProduct: Boolean) {
        binding?.header?.headerSubTitle = if (isVoucherProduct) {
            getString(R.string.smvc_creation_step_one_out_of_four_sub_title_label)
        } else {
            getString(R.string.smvc_creation_step_one_out_of_three_sub_title_label)
        }
    }

    private fun renderLoading(isLoading: Boolean) {
        binding?.btnContinue?.isLoading = isLoading
    }

    private fun renderVoucherTypeSelection(isVoucherProduct: Boolean) {
        if (isVoucherProduct) setVoucherProductSelected() else setVoucherShopSelected()
        binding?.btnContinue?.enable()
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
                VoucherCreationEvent.ChooseVoucherType(
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
        //TODO: implement dialog
    }

    private fun showError(error: Throwable) {
        binding?.btnContinue.showToasterError(error.message ?: "")
    }
}
