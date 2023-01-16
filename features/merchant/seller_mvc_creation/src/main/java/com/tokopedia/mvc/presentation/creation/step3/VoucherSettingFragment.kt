package com.tokopedia.mvc.presentation.creation.step3

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
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcFragmentCreationVoucherSettingBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepThreeButtonSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepThreeBuyerTargetSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepThreeCashbackInputSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepThreeDiscountInputSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepThreeFreeShippingInputSectionBinding
import com.tokopedia.mvc.databinding.SmvcVoucherCreationStepThreePromoTypeSectionBinding
import com.tokopedia.mvc.di.component.DaggerMerchantVoucherCreationComponent
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.presentation.bottomsheet.ExpenseEstimationBottomSheet
import com.tokopedia.mvc.presentation.creation.step2.VoucherInformationActivity
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeAction
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeEvent
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeUiState
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class VoucherSettingFragment : BaseDaggerFragment() {

    companion object {
        fun newInstance(
            pageMode: PageMode,
            voucherConfiguration: VoucherConfiguration
        ): VoucherSettingFragment {
            return VoucherSettingFragment().apply {
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
    private var binding by autoClearedNullable<SmvcFragmentCreationVoucherSettingBinding>()
    private var promoTypeSectionBinding by autoClearedNullable<SmvcVoucherCreationStepThreePromoTypeSectionBinding>()
    private var freeShippingInputSectionBinding by autoClearedNullable<SmvcVoucherCreationStepThreeFreeShippingInputSectionBinding>()
    private var cashbackInputSectionBinding by autoClearedNullable<SmvcVoucherCreationStepThreeCashbackInputSectionBinding>()
    private var discountInputSectionBinding by autoClearedNullable<SmvcVoucherCreationStepThreeDiscountInputSectionBinding>()
    private var buyerTargetSectionBinding by autoClearedNullable<SmvcVoucherCreationStepThreeBuyerTargetSectionBinding>()
    private var buttonSectionBinding by autoClearedNullable<SmvcVoucherCreationStepThreeButtonSectionBinding>()

    // viewModel
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(VoucherSettingViewModel::class.java) }

    // data
    private val pageMode by lazy { arguments?.getParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE) as? PageMode }
    private val voucherConfiguration by lazy {
        arguments?.getParcelable(BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION) as? VoucherConfiguration
            ?: VoucherConfiguration()
    }

    override fun getScreenName(): String =
        VoucherSettingFragment::class.java.canonicalName.orEmpty()

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
        binding = SmvcFragmentCreationVoucherSettingBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.processEvent(
            VoucherCreationStepThreeEvent.InitVoucherConfiguration(
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

    private fun handleUiState(state: VoucherCreationStepThreeUiState) {
        when (state.voucherConfiguration.promoType) {
            PromoType.FREE_SHIPPING -> {
                // free shipping input
                renderFreeShippingNominalInputValidation(state.isNominalError, state.nominalErrorMsg)
                renderFreeShippingMinimumBuyInputValidation(state.isMinimumBuyError, state.minimumBuyErrorMsg)
                renderFreeShippingQuotaInputValidation(state.isQuotaError, state.quotaErrorMsg)
            }
            PromoType.CASHBACK -> {
                // cashback input
                renderCashbackNominalInputValidation(state.isNominalError, state.nominalErrorMsg)
                renderCashbackPercentageInputValidation(state.isPercentageError, state.percentageErrorMsg)
                renderCashbackMaxDeductionInputValidation(state.isMaxDeductionError, state.maxDeductionErrorMsg)
                renderCashbackMinimumBuyInputValidation(state.isMinimumBuyError, state.minimumBuyErrorMsg)
                renderCashbackQuotaInputValidation(state.isQuotaError, state.quotaErrorMsg)
            }
            PromoType.DISCOUNT -> {
                // discount input
                renderDiscountNominalInputValidation(state.isNominalError, state.nominalErrorMsg)
                renderDiscountPercentageInputValidation(state.isPercentageError, state.percentageErrorMsg)
                renderDiscountMaxDeductionInputValidation(state.isMaxDeductionError, state.maxDeductionErrorMsg)
                renderDiscountMinimumBuyInputValidation(state.isMinimumBuyError, state.minimumBuyErrorMsg)
                renderDiscountQuotaInputValidation(state.isQuotaError, state.quotaErrorMsg)
            }
        }
        renderAvailableTargetBuyer(state.availableTargetBuyer, state.voucherConfiguration)
        renderSpendingEstimation(state.spendingEstimation)
        renderButtonValidation(state.voucherConfiguration, state.isInputValid())
    }

    private fun handleAction(action: VoucherCreationStepThreeAction) {
        when (action) {
            is VoucherCreationStepThreeAction.BackToPreviousStep -> backToPreviousStep(action.voucherConfiguration)
            is VoucherCreationStepThreeAction.ContinueToNextStep -> TODO()
            is VoucherCreationStepThreeAction.ShowCoachmark -> TODO()
            is VoucherCreationStepThreeAction.ShowError -> TODO()
        }
    }

    private fun setupView() {
        binding?.run {
            viewPromoType.setOnInflateListener { _, view ->
                promoTypeSectionBinding =
                    SmvcVoucherCreationStepThreePromoTypeSectionBinding.bind(view)
            }
            viewFreeShippingInput.setOnInflateListener { _, view ->
                freeShippingInputSectionBinding =
                    SmvcVoucherCreationStepThreeFreeShippingInputSectionBinding.bind(view)
            }
            viewCashbackInput.setOnInflateListener { _, view ->
                cashbackInputSectionBinding =
                    SmvcVoucherCreationStepThreeCashbackInputSectionBinding.bind(view)
            }
            viewDiscountInput.setOnInflateListener { _, view ->
                discountInputSectionBinding =
                    SmvcVoucherCreationStepThreeDiscountInputSectionBinding.bind(view)
            }
            viewBuyerTarget.setOnInflateListener { _, view ->
                buyerTargetSectionBinding =
                    SmvcVoucherCreationStepThreeBuyerTargetSectionBinding.bind(view)
            }
            viewButton.setOnInflateListener { _, view ->
                buttonSectionBinding = SmvcVoucherCreationStepThreeButtonSectionBinding.bind(view)
            }
            setupHeader()
            setupPromoTypeSection()
            setupTargetBuyerSection()
            setupSpendingEstimationSection()
            setupButtonSection()
        }
    }

    private fun setupHeader() {
        binding?.header?.apply {
            headerSubTitle = if (voucherConfiguration.isVoucherProduct) {
                getString(R.string.smvc_creation_step_three_out_of_four_sub_title_label)
            } else {
                getString(R.string.smvc_creation_step_three_out_of_three_sub_title_label)
            }
            setNavigationOnClickListener {
                viewModel.processEvent(VoucherCreationStepThreeEvent.TapBackButton)
            }
        }
    }

    // Promo type input region
    private fun setupPromoTypeSection() {
        binding?.run {
            if (viewPromoType.parent != null) {
                viewPromoType.inflate()
            }
        }
        setupInputSection()
    }

    private fun setupInputSection() {
        binding?.run {
            if (viewFreeShippingInput.parent != null) {
                viewFreeShippingInput.inflate()
            }
            if (viewCashbackInput.parent != null) {
                viewCashbackInput.inflate()
            }
            if (viewDiscountInput.parent != null) {
                viewDiscountInput.inflate()
            }
        }
        setupPromoTypeSelection()
    }

    private fun setupPromoTypeSelection() {
        val currentVoucherConfiguration = viewModel.getCurrentVoucherConfiguration()
        val selectedPromoType = currentVoucherConfiguration.promoType
        setPromoType(voucherConfiguration.promoType)
        promoTypeSectionBinding?.run {
            when (selectedPromoType) {
                PromoType.FREE_SHIPPING -> {
                    setFreeShippingSelected()
                }
                PromoType.CASHBACK -> {
                    setCashbackSelected()
                }
                PromoType.DISCOUNT -> {
                    setDiscountSelected()
                }
            }
            chipFreeShipping.chip_container.setOnClickListener {
                setFreeShippingSelected()
                setPromoType(PromoType.FREE_SHIPPING)
            }
            chipCashback.chip_container.setOnClickListener {
                setCashbackSelected()
                setPromoType(PromoType.CASHBACK)
            }
            chipDiscount.chip_container.setOnClickListener {
                setDiscountSelected()
                setPromoType(PromoType.DISCOUNT)
            }
        }
    }

    private fun setPromoType(promoType: PromoType) {
        viewModel.processEvent(VoucherCreationStepThreeEvent.ChoosePromoType(promoType))
    }

    // Free shipping input region
    private fun setFreeShippingSelected() {
        val currentVoucherConfiguration = viewModel.getCurrentVoucherConfiguration()
        promoTypeSectionBinding?.run {
            chipFreeShipping.chipType = ChipsUnify.TYPE_SELECTED
            chipCashback.chipType = ChipsUnify.TYPE_NORMAL
            chipDiscount.chipType = ChipsUnify.TYPE_NORMAL
        }
        binding?.run {
            viewFreeShippingInput.visible()
            viewCashbackInput.gone()
            viewDiscountInput.gone()
        }
        setFreeShippingNominalInput(currentVoucherConfiguration)
        setFreeShippingMinimumBuyInput(currentVoucherConfiguration)
        setFreeShippingQuotaInput(currentVoucherConfiguration)
        viewModel.processEvent(
            VoucherCreationStepThreeEvent.ChooseBenefitType(
                BenefitType.NOMINAL
            )
        )
    }

    private fun setFreeShippingNominalInput(currentVoucherConfiguration: VoucherConfiguration) {
        freeShippingInputSectionBinding?.run {
            tfFreeShippingNominal.apply {
                editText.setText(currentVoucherConfiguration.benefitIdr.toString())
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputNominalChanged(it.toLong())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setFreeShippingMinimumBuyInput(currentVoucherConfiguration: VoucherConfiguration) {
        freeShippingInputSectionBinding?.run {
            tfFreeShippingMinimumBuy.apply {
                editText.setText(currentVoucherConfiguration.minPurchase.toString())
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(it.toLong())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setFreeShippingQuotaInput(currentVoucherConfiguration: VoucherConfiguration) {
        freeShippingInputSectionBinding?.run {
            tfFreeShippingQuota.apply {
                editText.setText(currentVoucherConfiguration.quota.toString())
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputQuotaChanged(it.toLong())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun renderFreeShippingNominalInputValidation(
        isFreeShippingNominalError: Boolean,
        freeShippingNominalErrorMsg: String
    ) {
        freeShippingInputSectionBinding?.run {
            tfFreeShippingNominal.isInputError = isFreeShippingNominalError
            tfFreeShippingNominal.setMessage(freeShippingNominalErrorMsg)
        }
    }

    private fun renderFreeShippingMinimumBuyInputValidation(
        isFreeShippingMinimumBuyError: Boolean,
        freeShippingMinimumBuyErrorMsg: String
    ) {
        freeShippingInputSectionBinding?.run {
            tfFreeShippingMinimumBuy.isInputError = isFreeShippingMinimumBuyError
            tfFreeShippingMinimumBuy.setMessage(freeShippingMinimumBuyErrorMsg)
        }
    }

    private fun renderFreeShippingQuotaInputValidation(
        isFreeShippingQuotaError: Boolean,
        freeShippingQuotaErrorMsg: String
    ) {
        freeShippingInputSectionBinding?.run {
            tfFreeShippingQuota.isInputError = isFreeShippingQuotaError
            tfFreeShippingQuota.setMessage(freeShippingQuotaErrorMsg)
        }
    }

    // Cashback input region
    private fun setCashbackSelected() {
        val currentVoucherConfiguration = viewModel.getCurrentVoucherConfiguration()
        promoTypeSectionBinding?.run {
            chipFreeShipping.chipType = ChipsUnify.TYPE_NORMAL
            chipCashback.chipType = ChipsUnify.TYPE_SELECTED
            chipDiscount.chipType = ChipsUnify.TYPE_NORMAL
        }
        binding?.run {
            viewFreeShippingInput.gone()
            viewCashbackInput.visible()
            viewDiscountInput.gone()
        }
        setCashbackSwitchPriceInput(currentVoucherConfiguration)
        setCashbackMaxDeductionInput(currentVoucherConfiguration)
        setCashbackMinimumBuyInput(currentVoucherConfiguration)
        setCashbackQuotaInput(currentVoucherConfiguration)
    }

    private fun setCashbackSwitchPriceInput(currentVoucherConfiguration: VoucherConfiguration) {
        cashbackInputSectionBinding?.run {
            val isChecked = currentVoucherConfiguration.benefitType == BenefitType.PERCENTAGE
            if (isChecked) {
                setCashbackPercentageInput(currentVoucherConfiguration)
            } else {
                setCashbackNominalInput(currentVoucherConfiguration)
            }
            switchPriceCashback.isChecked = isChecked
            switchPriceCashback.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    setCashbackPercentageInput(currentVoucherConfiguration)
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.ChooseBenefitType(
                            BenefitType.PERCENTAGE
                        )
                    )
                } else {
                    setCashbackNominalInput(currentVoucherConfiguration)
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.ChooseBenefitType(
                            BenefitType.NOMINAL
                        )
                    )
                }
            }
        }
    }

    private fun setCashbackNominalInput(currentVoucherConfiguration: VoucherConfiguration) {
        cashbackInputSectionBinding?.run {
            tfCashbackNominal.apply {
                visible()
                tpgCashbackMaxDeductionLabel.gone()
                tfCahsbackMaxDeduction.gone()
                appendText("")
                prependText(getString(R.string.smvc_rupiah_label))
                labelText.text = getString(R.string.smvc_nominal_cashback_label)
                editText.setText(currentVoucherConfiguration.benefitIdr.toString())
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputNominalChanged(it.toLong())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
            tfCashbackPercentage.invisible()
        }
    }

    private fun setCashbackPercentageInput(currentVoucherConfiguration: VoucherConfiguration) {
        cashbackInputSectionBinding?.run {
            tfCashbackPercentage.apply {
                visible()
                tpgCashbackMaxDeductionLabel.visible()
                tfCahsbackMaxDeduction.visible()
                appendText(getString(R.string.smvc_percent_symbol))
                prependText("")
                labelText.text = getString(R.string.smvc_percentage_label)
                editText.setText(currentVoucherConfiguration.benefitPercent.toString())
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputPercentageChanged(it.toIntOrZero())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
            tfCashbackNominal.invisible()
        }
    }

    private fun setCashbackMaxDeductionInput(currentVoucherConfiguration: VoucherConfiguration) {
        cashbackInputSectionBinding?.run {
            tfCahsbackMaxDeduction.apply {
                editText.setText(currentVoucherConfiguration.benefitMax.toString())
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMaxDeductionChanged(it.toLong())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setCashbackMinimumBuyInput(currentVoucherConfiguration: VoucherConfiguration) {
        cashbackInputSectionBinding?.run {
            tfCashbackMinimumBuy.apply {
                editText.setText(currentVoucherConfiguration.minPurchase.toString())
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(it.toLong())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setCashbackQuotaInput(currentVoucherConfiguration: VoucherConfiguration) {
        cashbackInputSectionBinding?.run {
            tfCashbackQuota.apply {
                editText.setText(currentVoucherConfiguration.quota.toString())
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputQuotaChanged(it.toLong())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun renderCashbackNominalInputValidation(
        isCashbackNominalInputError: Boolean,
        cashbackNominalInputErrorMsg: String
    ) {
        cashbackInputSectionBinding?.run {
            tfCashbackNominal.isInputError = isCashbackNominalInputError
            tfCashbackNominal.setMessage(cashbackNominalInputErrorMsg)
        }
    }

    private fun renderCashbackPercentageInputValidation(
        isCashbackPercentageInputError: Boolean,
        cashbackPercentageInputErrorMsg: String
    ) {
        cashbackInputSectionBinding?.run {
            tfCashbackPercentage.isInputError = isCashbackPercentageInputError
            tfCashbackPercentage.setMessage(cashbackPercentageInputErrorMsg)
        }
    }

    private fun renderCashbackMaxDeductionInputValidation(
        isCashbackMaxDeductionInputError: Boolean,
        cashbackMaxDeductionInputErrorMsg: String
    ) {
        cashbackInputSectionBinding?.run {
            tfCahsbackMaxDeduction.isInputError = isCashbackMaxDeductionInputError
            tfCahsbackMaxDeduction.setMessage(cashbackMaxDeductionInputErrorMsg)
        }
    }

    private fun renderCashbackMinimumBuyInputValidation(
        isCashbackMinimunBuyInputError: Boolean,
        cashbackMinimumBuyInputErrorMsg: String
    ) {
        cashbackInputSectionBinding?.run {
            tfCashbackMinimumBuy.isInputError = isCashbackMinimunBuyInputError
            tfCashbackMinimumBuy.setMessage(cashbackMinimumBuyInputErrorMsg)
        }
    }

    private fun renderCashbackQuotaInputValidation(
        isCashbackQuotaInputError: Boolean,
        cashbackQuotaInputErrorMsg: String
    ) {
        cashbackInputSectionBinding?.run {
            tfCashbackQuota.isInputError = isCashbackQuotaInputError
            tfCashbackQuota.setMessage(cashbackQuotaInputErrorMsg)
        }
    }

    // Discount input region
    private fun setDiscountSelected() {
        val currentVoucherConfiguration = viewModel.getCurrentVoucherConfiguration()
        promoTypeSectionBinding?.run {
            chipFreeShipping.chipType = ChipsUnify.TYPE_NORMAL
            chipCashback.chipType = ChipsUnify.TYPE_NORMAL
            chipDiscount.chipType = ChipsUnify.TYPE_SELECTED
        }
        binding?.run {
            viewFreeShippingInput.gone()
            viewCashbackInput.gone()
            viewDiscountInput.visible()
        }
        setDiscountSwitchPriceInput(currentVoucherConfiguration)
        setDiscountMaxDeductionInput(currentVoucherConfiguration)
        setDiscountMinimumBuyInput(currentVoucherConfiguration)
        setDiscountQuotaInput(currentVoucherConfiguration)
    }

    private fun setDiscountSwitchPriceInput(currentVoucherConfiguration: VoucherConfiguration) {
        discountInputSectionBinding?.run {
            val isChecked = currentVoucherConfiguration.benefitType == BenefitType.PERCENTAGE
            if (isChecked) {
                setDiscountPercentageInput(currentVoucherConfiguration)
            } else {
                setDiscountNominalInput(currentVoucherConfiguration)
            }
            switchPriceDiscount.isChecked = isChecked
            switchPriceDiscount.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    setDiscountPercentageInput(currentVoucherConfiguration)
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.ChooseBenefitType(
                            BenefitType.PERCENTAGE
                        )
                    )
                } else {
                    setDiscountNominalInput(currentVoucherConfiguration)
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.ChooseBenefitType(
                            BenefitType.NOMINAL
                        )
                    )
                }
            }
        }
    }

    private fun setDiscountNominalInput(currentVoucherConfiguration: VoucherConfiguration) {
        discountInputSectionBinding?.run {
            tfDiscountNominal.apply {
                visible()
                tpgDiscountMaxDeductionLabel.gone()
                tfDiscountMaxDeduction.gone()
                appendText("")
                prependText(getString(R.string.smvc_rupiah_label))
                labelText.text = getString(R.string.smvc_nominal_discount_label)
                editText.setText(currentVoucherConfiguration.benefitIdr.toString())
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputNominalChanged(it.toLong())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
            tfDiscountPercentage.invisible()
        }
    }

    private fun setDiscountPercentageInput(currentVoucherConfiguration: VoucherConfiguration) {
        discountInputSectionBinding?.run {
            tfDiscountPercentage.apply {
                visible()
                tpgDiscountMaxDeductionLabel.visible()
                tfDiscountMaxDeduction.visible()
                appendText(getString(R.string.smvc_percent_symbol))
                prependText("")
                labelText.text = getString(R.string.smvc_percentage_label)
                editText.setText(currentVoucherConfiguration.benefitPercent.toString())
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputPercentageChanged(it.toIntOrZero())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
            tfDiscountNominal.invisible()
        }
    }

    private fun setDiscountMaxDeductionInput(currentVoucherConfiguration: VoucherConfiguration) {
        discountInputSectionBinding?.run {
            tfDiscountMaxDeduction.apply {
                editText.setText(currentVoucherConfiguration.benefitMax.toString())
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMaxDeductionChanged(it.toLong())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setDiscountMinimumBuyInput(currentVoucherConfiguration: VoucherConfiguration) {
        discountInputSectionBinding?.run {
            tfDiscountMinimumBuy.apply {
                editText.setText(currentVoucherConfiguration.minPurchase.toString())
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(it.toLong())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setDiscountQuotaInput(currentVoucherConfiguration: VoucherConfiguration) {
        discountInputSectionBinding?.run {
            tfDiscountQuota.apply {
                editText.setText(currentVoucherConfiguration.quota.toString())
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputQuotaChanged(it.toLong())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun renderDiscountNominalInputValidation(
        isDiscountNominalInputError: Boolean,
        discountNominalInputErrorMsg: String
    ) {
        discountInputSectionBinding?.run {
            tfDiscountNominal.isInputError = isDiscountNominalInputError
            tfDiscountNominal.setMessage(discountNominalInputErrorMsg)
        }
    }

    private fun renderDiscountPercentageInputValidation(
        isDiscountPercentageInputError: Boolean,
        discountPercentageInputErrorMsg: String
    ) {
        discountInputSectionBinding?.run {
            tfDiscountPercentage.isInputError = isDiscountPercentageInputError
            tfDiscountPercentage.setMessage(discountPercentageInputErrorMsg)
        }
    }

    private fun renderDiscountMaxDeductionInputValidation(
        isDiscountMaxDeductionInputError: Boolean,
        discountMaxDeductionInputErrorMsg: String
    ) {
        discountInputSectionBinding?.run {
            tfDiscountMaxDeduction.isInputError = isDiscountMaxDeductionInputError
            tfDiscountMaxDeduction.setMessage(discountMaxDeductionInputErrorMsg)
        }
    }

    private fun renderDiscountMinimumBuyInputValidation(
        isDiscountMinimunBuyInputError: Boolean,
        discountMinimumBuyInputErrorMsg: String
    ) {
        discountInputSectionBinding?.run {
            tfDiscountMinimumBuy.isInputError = isDiscountMinimunBuyInputError
            tfDiscountMinimumBuy.setMessage(discountMinimumBuyInputErrorMsg)
        }
    }

    private fun renderDiscountQuotaInputValidation(
        isDiscountQuotaInputError: Boolean,
        discountQuotaInputErrorMsg: String
    ) {
        discountInputSectionBinding?.run {
            tfDiscountQuota.isInputError = isDiscountQuotaInputError
            tfDiscountQuota.setMessage(discountQuotaInputErrorMsg)
        }
    }

    // Target buyer region
    private fun setupTargetBuyerSection() {
        binding?.run {
            if (viewBuyerTarget.parent != null) {
                viewBuyerTarget.inflate()
            }
        }
        setupTargetBuyerSelection()
    }

    private fun setupTargetBuyerSelection() {
        buyerTargetSectionBinding?.run {
            val currentVoucherConfiguration = viewModel.getCurrentVoucherConfiguration()
            rgBuyerTarget.apply {
                setOnCheckedChangeListener { _, position ->
                    val target = if (position == VoucherTargetBuyer.ALL_BUYER.id) {
                        VoucherTargetBuyer.ALL_BUYER
                    } else {
                        VoucherTargetBuyer.NEW_FOLLOWER
                    }
                    viewModel.processEvent(VoucherCreationStepThreeEvent.ChooseTargetBuyer(target))
                }
            }
            setTargetBuyerRadioButton(currentVoucherConfiguration)
        }
    }

    private fun setTargetBuyerRadioButton(voucherConfiguration: VoucherConfiguration) {
        buyerTargetSectionBinding?.run {
            if (voucherConfiguration.targetBuyer == VoucherTargetBuyer.ALL_BUYER) {
                radioAllBuyer.isChecked = true
            } else {
                radioNewFollower.isChecked = true
            }
        }
    }

    private fun renderAvailableTargetBuyer(availableTargetBuyer: List<VoucherTargetBuyer>, voucherConfiguration: VoucherConfiguration) {
        buyerTargetSectionBinding?.run {
            radioAllBuyer.disable()
            radioNewFollower.disable()
            for (id in availableTargetBuyer) {
                rgBuyerTarget.getChildAt(id.id).enable()
            }
            setTargetBuyerRadioButton(voucherConfiguration)
        }
    }

    // Spending estimation section
    private fun setupSpendingEstimationSection() {
        binding?.run {
            labelSpendingEstimation.apply {
                titleText = getString(R.string.smvc_spending_estimation_title_1)
                descriptionText = getString(R.string.smvc_spending_estimation_description_1)
                iconInfo?.setOnClickListener {
                    ExpenseEstimationBottomSheet.newInstance().show(childFragmentManager)
                }
            }
        }
    }

    private fun renderSpendingEstimation(spendingEstimation: Long) {
        binding?.run {
            labelSpendingEstimation.spendingEstimationText = spendingEstimation.getCurrencyFormatted()
        }
    }

    // Button region
    private fun setupButtonSection() {
        binding?.run {
            if (viewButton.parent != null) {
                viewButton.inflate()
            }
        }

        buttonSectionBinding?.run {
            btnBack.setOnClickListener { viewModel.processEvent(VoucherCreationStepThreeEvent.TapBackButton) }
        }
    }

    private fun backToPreviousStep(voucherConfiguration: VoucherConfiguration) {
        if (pageMode == PageMode.CREATE) {
            VoucherInformationActivity.start(requireContext(), voucherConfiguration)
            activity?.finish()
        } else {
            // TODO: navigate back to summary page
        }
    }

    private fun renderButtonValidation(
        voucherConfiguration: VoucherConfiguration,
        isEnabled: Boolean
    ) {
        buttonSectionBinding?.run {
            btnContinue.apply {
                this.isEnabled = isEnabled
                setOnClickListener {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.NavigateToNextStep(
                            voucherConfiguration
                        )
                    )
                }
            }
        }
    }
}
