package com.tokopedia.mvc.presentation.creation.step3

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
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
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
import com.tokopedia.mvc.domain.entity.enums.*
import com.tokopedia.mvc.presentation.bottomsheet.ExpenseEstimationBottomSheet
import com.tokopedia.mvc.presentation.creation.step2.VoucherInformationActivity
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeAction
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeEvent
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeUiState
import com.tokopedia.mvc.presentation.product.add.AddProductActivity
import com.tokopedia.mvc.presentation.summary.SummaryActivity
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import java.util.ArrayList
import javax.inject.Inject

@FlowPreview
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

    // coachmark
    private val coachMark by lazy {
        context?.let {
            CoachMark2(it)
        }
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
                pageMode ?: PageMode.CREATE,
                voucherConfiguration
            )
        )
        setupView()
        observeUiState()
        observeUiAction()
        viewModel.processEvent(VoucherCreationStepThreeEvent.HandleCoachMark)
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
                renderFreeShippingValidation(state)
            }
            PromoType.CASHBACK -> {
                renderCashbackValidation(state)
            }
            PromoType.DISCOUNT -> {
                renderDiscountValidation(state)
            }
        }
        renderAvailableTargetBuyer(state.availableTargetBuyer, state.voucherConfiguration)
        renderSpendingEstimation(state.spendingEstimation)
        renderButtonValidation(state.voucherConfiguration, state.isInputValid())
    }

    private fun renderFreeShippingValidation(state: VoucherCreationStepThreeUiState) {
        when (state.fieldValidated) {
            VoucherCreationStepThreeFieldValidation.NOMINAL -> {
                renderFreeShippingNominalInputValidation(
                    state.isNominalError,
                    state.nominalErrorMsg
                )
            }
            VoucherCreationStepThreeFieldValidation.MINIMUM_BUY -> {
                renderFreeShippingMinimumBuyInputValidation(
                    state.isMinimumBuyError,
                    state.minimumBuyErrorMsg
                )
            }
            VoucherCreationStepThreeFieldValidation.QUOTA -> {
                renderFreeShippingQuotaInputValidation(state.isQuotaError, state.quotaErrorMsg)
            }
            VoucherCreationStepThreeFieldValidation.ALL -> {
                renderFreeShippingNominalInputValidation(
                    state.isNominalError,
                    state.nominalErrorMsg
                )
                renderFreeShippingMinimumBuyInputValidation(
                    state.isMinimumBuyError,
                    state.minimumBuyErrorMsg
                )
                renderFreeShippingQuotaInputValidation(state.isQuotaError, state.quotaErrorMsg)
            }
        }
    }

    private fun renderCashbackValidation(state: VoucherCreationStepThreeUiState) {
        when (state.fieldValidated) {
            VoucherCreationStepThreeFieldValidation.NOMINAL -> {
                renderCashbackNominalInputValidation(state.isNominalError, state.nominalErrorMsg)
            }
            VoucherCreationStepThreeFieldValidation.PERCENTAGE -> {
                renderCashbackPercentageInputValidation(
                    state.isPercentageError,
                    state.percentageErrorMsg
                )
            }
            VoucherCreationStepThreeFieldValidation.MAX_DEDUCTION -> {
                renderCashbackMaxDeductionInputValidation(
                    state.isMaxDeductionError,
                    state.maxDeductionErrorMsg
                )
            }
            VoucherCreationStepThreeFieldValidation.MINIMUM_BUY -> {
                renderCashbackMinimumBuyInputValidation(
                    state.isMinimumBuyError,
                    state.minimumBuyErrorMsg
                )
            }
            VoucherCreationStepThreeFieldValidation.QUOTA -> {
                renderCashbackQuotaInputValidation(state.isQuotaError, state.quotaErrorMsg)
            }
            VoucherCreationStepThreeFieldValidation.ALL -> {
                renderCashbackNominalInputValidation(state.isNominalError, state.nominalErrorMsg)
                renderCashbackPercentageInputValidation(
                    state.isPercentageError,
                    state.percentageErrorMsg
                )
                renderCashbackMaxDeductionInputValidation(
                    state.isMaxDeductionError,
                    state.maxDeductionErrorMsg
                )
                renderCashbackMinimumBuyInputValidation(
                    state.isMinimumBuyError,
                    state.minimumBuyErrorMsg
                )
                renderCashbackQuotaInputValidation(state.isQuotaError, state.quotaErrorMsg)
            }
        }
    }

    private fun renderDiscountValidation(state: VoucherCreationStepThreeUiState) {
        when (state.fieldValidated) {
            VoucherCreationStepThreeFieldValidation.NOMINAL -> {
                renderDiscountNominalInputValidation(state.isNominalError, state.nominalErrorMsg)
            }
            VoucherCreationStepThreeFieldValidation.PERCENTAGE -> {
                renderDiscountPercentageInputValidation(
                    state.isPercentageError,
                    state.percentageErrorMsg
                )
            }
            VoucherCreationStepThreeFieldValidation.MAX_DEDUCTION -> {
                renderDiscountMaxDeductionInputValidation(
                    state.isMaxDeductionError,
                    state.maxDeductionErrorMsg
                )
            }
            VoucherCreationStepThreeFieldValidation.MINIMUM_BUY -> {
                renderDiscountMinimumBuyInputValidation(
                    state.isMinimumBuyError,
                    state.minimumBuyErrorMsg
                )
            }
            VoucherCreationStepThreeFieldValidation.QUOTA -> {
                renderDiscountQuotaInputValidation(state.isQuotaError, state.quotaErrorMsg)
            }
            VoucherCreationStepThreeFieldValidation.ALL -> {
                renderDiscountNominalInputValidation(state.isNominalError, state.nominalErrorMsg)
                renderDiscountPercentageInputValidation(
                    state.isPercentageError,
                    state.percentageErrorMsg
                )
                renderDiscountMaxDeductionInputValidation(
                    state.isMaxDeductionError,
                    state.maxDeductionErrorMsg
                )
                renderDiscountMinimumBuyInputValidation(
                    state.isMinimumBuyError,
                    state.minimumBuyErrorMsg
                )
                renderDiscountQuotaInputValidation(state.isQuotaError, state.quotaErrorMsg)
            }
        }
    }

    private fun handleAction(action: VoucherCreationStepThreeAction) {
        when (action) {
            is VoucherCreationStepThreeAction.BackToPreviousStep -> backToPreviousStep(action.voucherConfiguration)
            is VoucherCreationStepThreeAction.ContinueToNextStep -> continueToNextStep(action.voucherConfiguration)
            is VoucherCreationStepThreeAction.ShowCoachmark -> showCoachmark()
            is VoucherCreationStepThreeAction.ShowError -> TODO()
        }
    }

    private fun showCoachmark() {
        val coachMarkItem = ArrayList<CoachMark2Item>()
        promoTypeSectionBinding?.run {
            coachMarkItem.add(
                CoachMark2Item(
                    chipFreeShipping,
                    getString(R.string.smvc_voucher_creation_step_three_first_coachmark_title),
                    getString(R.string.smvc_voucher_creation_step_three_first_coachmark_description),
                    CoachMark2.POSITION_BOTTOM
                )
            )
        }
        buyerTargetSectionBinding?.run {
            coachMarkItem.add(
                CoachMark2Item(
                    tpgDiscountDeductionTypeLabel,
                    getString(R.string.smvc_voucher_creation_step_three_second_coachmark_title),
                    getString(R.string.smvc_voucher_creation_step_three_second_coachmark_description),
                    CoachMark2.POSITION_TOP
                )
            )
        }
        coachMark?.showCoachMark(coachMarkItem)
        coachMark?.onDismissListener = { viewModel.setSharedPrefCoachMarkAlreadyShown() }
        coachMark?.onFinishListener = { viewModel.setSharedPrefCoachMarkAlreadyShown() }
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
            presetValue()
        }
    }

    private fun presetValue() {
        val currentVoucherConfiguration = viewModel.getCurrentVoucherConfiguration()
        if (pageMode == PageMode.EDIT) {
            freeShippingInputSectionBinding?.run {
                tfFreeShippingNominal.editText.setText(currentVoucherConfiguration.benefitIdr.toString())
                tfFreeShippingMinimumBuy.editText.setText(currentVoucherConfiguration.minPurchase.toString())
                tfFreeShippingQuota.editText.setText(currentVoucherConfiguration.quota.toString())
            }
            cashbackInputSectionBinding?.run {
                tfCashbackNominal.editText.setText(currentVoucherConfiguration.benefitIdr.toString())
                tfCashbackPercentage.editText.setText(currentVoucherConfiguration.benefitPercent.toString())
                tfCahsbackMaxDeduction.editText.setText(currentVoucherConfiguration.benefitMax.toString())
                tfCashbackMinimumBuy.editText.setText(currentVoucherConfiguration.minPurchase.toString())
                tfCashbackQuota.editText.setText(currentVoucherConfiguration.quota.toString())
            }
            discountInputSectionBinding?.run {
                tfDiscountNominal.editText.setText(currentVoucherConfiguration.benefitIdr.toString())
                tfDiscountPercentage.editText.setText(currentVoucherConfiguration.benefitPercent.toString())
                tfDiscountMaxDeduction.editText.setText(currentVoucherConfiguration.benefitMax.toString())
                tfDiscountMinimumBuy.editText.setText(currentVoucherConfiguration.minPurchase.toString())
                tfDiscountQuota.editText.setText(currentVoucherConfiguration.quota.toString())
            }
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
            if (pageMode == PageMode.CREATE) {
                chipFreeShipping.chip_container.setOnClickListener {
                    setPromoType(PromoType.FREE_SHIPPING)
                    setFreeShippingSelected()
                }
                chipCashback.chip_container.setOnClickListener {
                    setPromoType(PromoType.CASHBACK)
                    setCashbackSelected()
                }
                chipDiscount.chip_container.setOnClickListener {
                    setPromoType(PromoType.DISCOUNT)
                    setDiscountSelected()
                }
            }
        }
    }

    private fun setPromoType(promoType: PromoType) {
        viewModel.processEvent(VoucherCreationStepThreeEvent.ChoosePromoType(promoType))
    }

    // Free shipping input region
    private fun setFreeShippingSelected() {
        promoTypeSectionBinding?.run {
            chipFreeShipping.chipType = ChipsUnify.TYPE_SELECTED
            chipCashback.chipType = ChipsUnify.TYPE_NORMAL
            chipDiscount.chipType = ChipsUnify.TYPE_NORMAL
        }

        freeShippingInputSectionBinding?.parentFreeShipping?.visible()
        cashbackInputSectionBinding?.parentCashback?.gone()
        discountInputSectionBinding?.parentDiscount?.gone()

        setFreeShippingNominalInput()
        setFreeShippingMinimumBuyInput()
        setFreeShippingQuotaInput()
        viewModel.processEvent(
            VoucherCreationStepThreeEvent.ChooseBenefitType(
                BenefitType.NOMINAL
            )
        )
    }

    private fun setFreeShippingNominalInput() {
        freeShippingInputSectionBinding?.run {
            tfFreeShippingNominal.apply {
                if (editText.text.isNotEmpty()) {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.OnInputNominalChanged(editText.text.toString())
                    )
                }
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputNominalChanged(it)
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setFreeShippingMinimumBuyInput() {
        freeShippingInputSectionBinding?.run {
            tfFreeShippingMinimumBuy.apply {
                if (editText.text.isNotEmpty()) {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(editText.text.toString())
                    )
                }
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(it)
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setFreeShippingQuotaInput() {
        freeShippingInputSectionBinding?.run {
            tfFreeShippingQuota.apply {
                if (editText.text.isNotEmpty()) {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.OnInputQuotaChanged(editText.text.toString())
                    )
                }
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputQuotaChanged(it)
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

        freeShippingInputSectionBinding?.parentFreeShipping?.gone()
        cashbackInputSectionBinding?.parentCashback?.visible()
        discountInputSectionBinding?.parentDiscount?.gone()

        setCashbackSwitchPriceInput(currentVoucherConfiguration)
        setCashbackMaxDeductionInput()
        setCashbackMinimumBuyInput()
        setCashbackQuotaInput()
    }

    private fun setCashbackSwitchPriceInput(currentVoucherConfiguration: VoucherConfiguration) {
        cashbackInputSectionBinding?.run {
            val isChecked = currentVoucherConfiguration.benefitType == BenefitType.PERCENTAGE
            if (isChecked) {
                setCashbackPercentageInput()
            } else {
                setCashbackNominalInput()
            }
            switchPriceCashback.isChecked = isChecked
            switchPriceCashback.setOnCheckedChangeListener { _, isOn ->
                if (isOn) {
                    setCashbackPercentageInput()
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.ChooseBenefitType(
                            BenefitType.PERCENTAGE
                        )
                    )
                } else {
                    setCashbackNominalInput()
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.ChooseBenefitType(
                            BenefitType.NOMINAL
                        )
                    )
                }
            }
        }
    }

    private fun setCashbackNominalInput() {
        cashbackInputSectionBinding?.run {
            tfCashbackNominal.apply {
                if (editText.text.isNotEmpty()) {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.OnInputNominalChanged(editText.text.toString())
                    )
                }
                visible()
                tpgCashbackMaxDeductionLabel.gone()
                tfCahsbackMaxDeduction.gone()
                appendText("")
                prependText(getString(R.string.smvc_rupiah_label))
                labelText.text = getString(R.string.smvc_nominal_cashback_label)
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputNominalChanged(it)
                        )
                    }
                    .launchIn(lifecycleScope)
            }
            tfCashbackPercentage.invisible()
        }
    }

    private fun setCashbackPercentageInput() {
        cashbackInputSectionBinding?.run {
            tfCashbackPercentage.apply {
                if (editText.text.isNotEmpty()) {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.OnInputPercentageChanged(editText.text.toString())
                    )
                }
                visible()
                tpgCashbackMaxDeductionLabel.visible()
                tfCahsbackMaxDeduction.visible()
                appendText(getString(R.string.smvc_percent_symbol))
                prependText("")
                labelText.text = getString(R.string.smvc_percentage_label)
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputPercentageChanged(it)
                        )
                    }
                    .launchIn(lifecycleScope)
            }
            tfCashbackNominal.invisible()
        }
    }

    private fun setCashbackMaxDeductionInput() {
        cashbackInputSectionBinding?.run {
            tfCahsbackMaxDeduction.apply {
                if (editText.text.isNotEmpty()) {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.OnInputMaxDeductionChanged(editText.text.toString())
                    )
                }
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMaxDeductionChanged(it)
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setCashbackMinimumBuyInput() {
        cashbackInputSectionBinding?.run {
            tfCashbackMinimumBuy.apply {
                if (editText.text.isNotEmpty()) {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(editText.text.toString())
                    )
                }
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(it)
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setCashbackQuotaInput() {
        cashbackInputSectionBinding?.run {
            tfCashbackQuota.apply {
                if (editText.text.isNotEmpty()) {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.OnInputQuotaChanged(editText.text.toString())
                    )
                }
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputQuotaChanged(it)
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
        isCashbackMinimumBuyInputError: Boolean,
        cashbackMinimumBuyInputErrorMsg: String
    ) {
        cashbackInputSectionBinding?.run {
            tfCashbackMinimumBuy.isInputError = isCashbackMinimumBuyInputError
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

        freeShippingInputSectionBinding?.parentFreeShipping?.gone()
        cashbackInputSectionBinding?.parentCashback?.gone()
        discountInputSectionBinding?.parentDiscount?.visible()

        setDiscountSwitchPriceInput(currentVoucherConfiguration)
        setDiscountMaxDeductionInput()
        setDiscountMinimumBuyInput()
        setDiscountQuotaInput()
    }

    private fun setDiscountSwitchPriceInput(currentVoucherConfiguration: VoucherConfiguration) {
        discountInputSectionBinding?.run {
            val isChecked = currentVoucherConfiguration.benefitType == BenefitType.PERCENTAGE
            if (isChecked) {
                setDiscountPercentageInput()
            } else {
                setDiscountNominalInput()
            }
            switchPriceDiscount.isChecked = isChecked
            switchPriceDiscount.setOnCheckedChangeListener { _, isOn ->
                if (isOn) {
                    setDiscountPercentageInput()
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.ChooseBenefitType(
                            BenefitType.PERCENTAGE
                        )
                    )
                } else {
                    setDiscountNominalInput()
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.ChooseBenefitType(
                            BenefitType.NOMINAL
                        )
                    )
                }
            }
        }
    }

    private fun setDiscountNominalInput() {
        discountInputSectionBinding?.run {
            tfDiscountNominal.apply {
                if (editText.text.isNotEmpty()) {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.OnInputNominalChanged(editText.text.toString())
                    )
                }
                visible()
                tpgDiscountMaxDeductionLabel.gone()
                tfDiscountMaxDeduction.gone()
                appendText("")
                prependText(getString(R.string.smvc_rupiah_label))
                labelText.text = getString(R.string.smvc_nominal_discount_label)
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputNominalChanged(it)
                        )
                    }
                    .launchIn(lifecycleScope)
            }
            tfDiscountPercentage.invisible()
        }
    }

    private fun setDiscountPercentageInput() {
        discountInputSectionBinding?.run {
            tfDiscountPercentage.apply {
                if (editText.text.isNotEmpty()) {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.OnInputPercentageChanged(editText.text.toString())
                    )
                }
                visible()
                tpgDiscountMaxDeductionLabel.visible()
                tfDiscountMaxDeduction.visible()
                appendText(getString(R.string.smvc_percent_symbol))
                prependText("")
                labelText.text = getString(R.string.smvc_percentage_label)
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputPercentageChanged(it)
                        )
                    }
                    .launchIn(lifecycleScope)
            }
            tfDiscountNominal.invisible()
        }
    }

    private fun setDiscountMaxDeductionInput() {
        discountInputSectionBinding?.run {
            tfDiscountMaxDeduction.apply {
                if (editText.text.isNotEmpty()) {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.OnInputMaxDeductionChanged(editText.text.toString())
                    )
                }
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMaxDeductionChanged(it)
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setDiscountMinimumBuyInput() {
        discountInputSectionBinding?.run {
            tfDiscountMinimumBuy.apply {
                if (editText.text.isNotEmpty()) {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(editText.text.toString())
                    )
                }
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(it)
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setDiscountQuotaInput() {
        discountInputSectionBinding?.run {
            tfDiscountQuota.apply {
                if (editText.text.isNotEmpty()) {
                    viewModel.processEvent(
                        VoucherCreationStepThreeEvent.OnInputQuotaChanged(editText.text.toString())
                    )
                }
                editText.textChangesAsFlow()
                    .filterNot { it.isEmpty() }
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputQuotaChanged(it)
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
        isDiscountMinimumBuyInputError: Boolean,
        discountMinimumBuyInputErrorMsg: String
    ) {
        discountInputSectionBinding?.run {
            tfDiscountMinimumBuy.isInputError = isDiscountMinimumBuyInputError
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
                setOnCheckedChangeListener { _, radioButtonId ->
                    val target = if (radioButtonId == radioAllBuyer.id) {
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

    private fun renderAvailableTargetBuyer(
        availableTargetBuyer: List<VoucherTargetBuyer>,
        voucherConfiguration: VoucherConfiguration
    ) {
        buyerTargetSectionBinding?.run {
            radioAllBuyer.disable()
            radioNewFollower.disable()
            if (pageMode == PageMode.CREATE) {
                for (id in availableTargetBuyer) {
                    rgBuyerTarget.getChildAt(id.id).enable()
                }
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
            labelSpendingEstimation.spendingEstimationText =
                spendingEstimation.getCurrencyFormatted()
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
            btnContinue.text = if (pageMode == PageMode.CREATE) {
                getString(R.string.smvc_continue)
            } else {
                getText(R.string.smvc_save)
            }
        }
    }

    private fun backToPreviousStep(voucherConfiguration: VoucherConfiguration) {
        if (pageMode == PageMode.CREATE) {
            context?.let { ctx -> VoucherInformationActivity.buildCreateModeIntent(ctx, voucherConfiguration) }
            activity?.finish()
        } else {
            activity?.finish()
        }
    }

    private fun continueToNextStep(voucherConfiguration: VoucherConfiguration) {
        if (pageMode == PageMode.CREATE) {
            if (voucherConfiguration.isVoucherProduct) {
                navigateToAddProductPage(voucherConfiguration)
            } else {
                navigateToVoucherSummaryPage(voucherConfiguration)
            }
        } else {
            setResultForSummaryPage(voucherConfiguration)
        }
    }

    private fun navigateToAddProductPage(currentVoucherConfiguration: VoucherConfiguration) {
        if (pageMode == PageMode.CREATE) {
            context?.let { ctx -> AddProductActivity.buildCreateModeIntent(ctx, currentVoucherConfiguration) }
        } else {
            context?.let { ctx -> AddProductActivity.buildEditModeIntent(ctx, currentVoucherConfiguration) }
        }
    }

    private fun navigateToVoucherSummaryPage(currentVoucherConfiguration: VoucherConfiguration) {
        context?.let { ctx ->
            SummaryActivity.start(
                ctx,
                currentVoucherConfiguration
            )
        }
    }

    private fun setResultForSummaryPage(currentVoucherConfiguration: VoucherConfiguration) {
        val intent = Intent().apply {
            putExtra(
                BundleConstant.BUNDLE_KEY_VOUCHER_CREATION_STEP_THREE,
                currentVoucherConfiguration
            )
        }
        activity?.setResult(104, intent)
        activity?.finish()
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
