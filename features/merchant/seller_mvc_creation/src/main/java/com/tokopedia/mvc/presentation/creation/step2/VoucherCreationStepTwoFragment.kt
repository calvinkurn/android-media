package com.tokopedia.mvc.presentation.creation.step2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.textChangesAsFlow
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentVoucherCreationStepTwoBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoButtonSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoVoucherCodeSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoVoucherNameSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoVoucherPeriodSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepTwoVoucherTargetSectionBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.presentation.creation.step1.VoucherCreationStepOneActivity
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoAction
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoEvent
import com.tokopedia.mvc.presentation.creation.step2.uimodel.VoucherCreationStepTwoUiState
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.mvc.util.constant.ImageUrlConstant
import com.tokopedia.mvc.util.extension.setToAllCapsMode
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@FlowPreview
class VoucherCreationStepTwoFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance(
            pageMode: PageMode,
            voucherConfiguration: VoucherConfiguration
        ): VoucherCreationStepTwoFragment {
            return VoucherCreationStepTwoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                    putParcelable(
                        BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION,
                        voucherConfiguration
                    )
                }
            }
        }
        private const val DEBOUNCE = 300L
    }

    // binding
    private var binding by autoClearedNullable<SmvcFragmentVoucherCreationStepTwoBinding>()
    private var voucherTargetSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoVoucherTargetSectionBinding>()
    private var voucherNameSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoVoucherNameSectionBinding>()
    private var voucherCodeSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoVoucherCodeSectionBinding>()
    private var voucherPeriodSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoVoucherPeriodSectionBinding>()
    private var buttonSectionBinding by autoClearedNullable<SmvcVoucherCreationStepTwoButtonSectionBinding>()

    // viewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(VoucherCreationStepTwoViewModel::class.java) }

    // data
    private val pageMode by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val voucherConfiguration by lazy {
        arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration
            ?: VoucherConfiguration()
    }

    override fun getScreenName(): String =
        VoucherCreationStepTwoFragment::class.java.canonicalName.orEmpty()

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
        binding = SmvcFragmentVoucherCreationStepTwoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.processEvent(
            VoucherCreationStepTwoEvent.InitVoucherConfiguration(
                voucherConfiguration
            )
        )
        setupView()
        observeUiState()
        observeUiAction()
    }

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state -> handleUiState(state) }
        }
    }

    private fun observeUiAction() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiAction.collect { action -> handleAction(action) }
        }
    }

    private fun handleUiState(state: VoucherCreationStepTwoUiState) {
        renderVoucherTargetSelection(state.voucherConfiguration.isVoucherPublic)
        renderVoucherNameValidation(state.isVoucherNameError, state.voucherNameErrorMsg)
        renderVoucherCodeValidation(
            state.voucherConfiguration,
            state.isVoucherCodeError,
            state.voucherCodeErrorMsg
        )
        renderButtonValidation(state.isInputValid())
    }

    private fun handleAction(action: VoucherCreationStepTwoAction) {
        when (action) {
            is VoucherCreationStepTwoAction.BackToPreviousStep -> backToPreviousStep(action.voucherConfiguration)
            is VoucherCreationStepTwoAction.ContinueToNextStep -> TODO()
            is VoucherCreationStepTwoAction.ValidateVoucherNameInput -> TODO()
            is VoucherCreationStepTwoAction.ShowError -> TODO()
        }
    }

    private fun setupView() {
        binding?.run {
            viewVoucherTarget.setOnInflateListener { _, view ->
                voucherTargetSectionBinding =
                    SmvcVoucherCreationStepTwoVoucherTargetSectionBinding.bind(view)
            }
            viewVoucherName.setOnInflateListener { _, view ->
                voucherNameSectionBinding =
                    SmvcVoucherCreationStepTwoVoucherNameSectionBinding.bind(view)
            }
            viewVoucherCode.setOnInflateListener { _, view ->
                voucherCodeSectionBinding =
                    SmvcVoucherCreationStepTwoVoucherCodeSectionBinding.bind(view)
            }
            viewVoucherActivePeriod.setOnInflateListener { _, view ->
                voucherPeriodSectionBinding =
                    SmvcVoucherCreationStepTwoVoucherPeriodSectionBinding.bind(view)
            }
            viewButton.setOnInflateListener { _, view ->
                buttonSectionBinding =
                    SmvcVoucherCreationStepTwoButtonSectionBinding.bind(view)
            }
        }
        setupHeader()
        setupVoucherTargetSection()
        setupVoucherNameSection()
        setupVoucherCodeSection()
        setupButtonSection()
    }

    private fun setupHeader() {
        binding?.header?.apply {
            headerSubTitle = if (voucherConfiguration.isVoucherProduct) {
                getString(R.string.smvc_creation_step_two_out_of_four_sub_title_label)
            } else {
                getString(R.string.smvc_creation_step_two_out_of_three_sub_title_label)
            }
            setNavigationOnClickListener {
                viewModel.processEvent(VoucherCreationStepTwoEvent.TapBackButton)
            }
        }
    }

    private fun backToPreviousStep(voucherConfiguration: VoucherConfiguration) {
        if (pageMode == PageMode.CREATE) {
            VoucherCreationStepOneActivity.start(requireContext(), voucherConfiguration)
            activity?.finish()
        } else {
            // TODO: navigate back to summary page
        }
    }

    // Voucher target section
    private fun setupVoucherTargetSection() {
        binding?.run {
            if (viewVoucherTarget.parent != null) {
                viewVoucherTarget.inflate()
            }
        }
        setupVoucherTargetSelection()
    }

    private fun setupVoucherTargetSelection() {
        setVoucherTarget(voucherConfiguration.isVoucherPublic)
        voucherTargetSectionBinding?.run {
            viewVoucherTargetPublic.apply {
                imgVoucherTarget?.setImageUrl(ImageUrlConstant.IMAGE_URL_PUBLIC_VOUCHER)
                cardParent?.setOnClickListener {
                    setVoucherTarget(true)
                }
            }
            viewVoucherTargetPrivate.apply {
                imgVoucherTarget?.setImageUrl(ImageUrlConstant.IMAGE_URL_PRIVATE_VOUCHER)
                cardParent?.setOnClickListener {
                    setVoucherTarget(false)
                }
            }
        }
    }

    private fun setVoucherTarget(isPublic: Boolean) {
        viewModel.processEvent(VoucherCreationStepTwoEvent.ChooseVoucherTarget(isPublic))
    }

    private fun renderVoucherTargetSelection(isPublic: Boolean) {
        if (isPublic) {
            setVoucherPublicSelected()
        } else {
            setVoucherPrivateSelected()
        }
    }

    private fun setVoucherPublicSelected() {
        binding?.run {
            viewVoucherCode.gone()
        }
        voucherTargetSectionBinding?.run {
            viewVoucherTargetPublic.isActive = true
            viewVoucherTargetPrivate.isActive = false
        }
    }

    private fun setVoucherPrivateSelected() {
        binding?.run {
            viewVoucherCode.visible()
        }
        voucherTargetSectionBinding?.run {
            viewVoucherTargetPublic.isActive = false
            viewVoucherTargetPrivate.isActive = true
        }
    }

    // Voucher name section
    private fun setupVoucherNameSection() {
        binding?.run {
            if (viewVoucherName.parent != null) {
                viewVoucherName.inflate()
            }
        }

        voucherNameSectionBinding?.run {
            tfVoucherName.editText.setText(voucherConfiguration.voucherName)
            tfVoucherName.editText.textChangesAsFlow()
                .filterNot { it.isEmpty() }
                .debounce { DEBOUNCE }
                .distinctUntilChanged()
                .onEach {
                    val formattedText = it.uppercase(Locale.getDefault()).trim()
                    viewModel.processEvent(
                        VoucherCreationStepTwoEvent.OnVoucherNameChanged(formattedText)
                    )
                }
                .launchIn(lifecycleScope)
        }
    }

    private fun renderVoucherNameValidation(
        isVoucherNameError: Boolean,
        voucherNameErrorMsg: String
    ) {
        voucherNameSectionBinding?.run {
            tfVoucherName.isInputError = isVoucherNameError
            tfVoucherName.setMessage(voucherNameErrorMsg)
        }
    }

    // Voucher code section
    private fun setupVoucherCodeSection() {
        binding?.run {
            if (viewVoucherCode.parent != null) {
                viewVoucherCode.inflate()
            }
        }

        voucherCodeSectionBinding?.run {
            tfVoucherCode.apply {
                prependText(voucherConfiguration.voucherCodePrefix)
                editText.setText(voucherConfiguration.voucherCode)
                editText.setToAllCapsMode()
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce { DEBOUNCE }
                    .distinctUntilChanged()
                    .onEach {
                        val formattedText = it.uppercase(Locale.getDefault()).trim()
                        viewModel.processEvent(
                            VoucherCreationStepTwoEvent.OnVoucherCodeChanged(formattedText)
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun renderVoucherCodeValidation(
        voucherConfiguration: VoucherConfiguration,
        isVoucherCodeError: Boolean,
        voucherCodeErrorMsg: String
    ) {
        voucherCodeSectionBinding?.run {
            tfVoucherCode.editText.setText(voucherConfiguration.voucherCode)
            tfVoucherCode.isInputError = isVoucherCodeError
            tfVoucherCode.setMessage(voucherCodeErrorMsg)
        }
    }

    // Button section
    private fun setupButtonSection() {
        binding?.run {
            if (viewButton.parent != null) {
                viewButton.inflate()
            }
        }

        buttonSectionBinding?.run {
            btnBack.setOnClickListener { viewModel.processEvent(VoucherCreationStepTwoEvent.TapBackButton) }
        }
    }

    private fun renderButtonValidation(isEnabled: Boolean) {
        buttonSectionBinding?.run {
            btnContinue.apply {
                this.isEnabled = isEnabled
                setOnClickListener { viewModel.processEvent(VoucherCreationStepTwoEvent.NavigateToNextStep) }
            }
        }
    }
}
