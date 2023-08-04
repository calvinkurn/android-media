package com.tokopedia.mvc.presentation.creation.step3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.campaign.entity.RemoteTicker
import com.tokopedia.campaign.utils.extension.disable
import com.tokopedia.campaign.utils.extension.enable
import com.tokopedia.campaign.utils.extension.routeToUrl
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
import com.tokopedia.mvc.domain.entity.SelectedProduct
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.domain.entity.enums.BenefitType
import com.tokopedia.mvc.domain.entity.enums.PageMode
import com.tokopedia.mvc.domain.entity.enums.PromoType
import com.tokopedia.mvc.domain.entity.enums.VoucherCreationStepThreeFieldValidation
import com.tokopedia.mvc.domain.entity.enums.VoucherTargetBuyer
import com.tokopedia.mvc.presentation.bottomsheet.ExpenseEstimationBottomSheet
import com.tokopedia.mvc.presentation.bottomsheet.QuotaInformationBottomSheet
import com.tokopedia.mvc.presentation.creation.step2.VoucherInformationActivity
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeAction
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeEvent
import com.tokopedia.mvc.presentation.creation.step3.uimodel.VoucherCreationStepThreeUiState
import com.tokopedia.mvc.presentation.product.add.AddProductActivity
import com.tokopedia.mvc.presentation.summary.SummaryActivity
import com.tokopedia.mvc.util.constant.BundleConstant
import com.tokopedia.mvc.util.tracker.VoucherSettingTracker
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
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
            voucherConfiguration: VoucherConfiguration,
            selectedProducts: List<SelectedProduct>
        ): VoucherSettingFragment {
            return VoucherSettingFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BundleConstant.BUNDLE_KEY_PAGE_MODE, pageMode)
                    putParcelable(
                        BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION,
                        voucherConfiguration
                    )
                    putParcelableArrayList(
                        BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS,
                        ArrayList(selectedProducts)
                    )
                }
            }
        }

        private const val DEBOUNCE = 300L
        private const val NOMINAL_INPUT_MAX_LENGTH = 11
        private const val PERCENTAGE_INPUT_MAX_LENGTH = 3
        private const val QUOTA_INPUT_MAX_LENGTH = 5
        private const val FREE_SHIPPING_EVENT_LABEL = "gratis ongkir"
        private const val CASHBACK_EVENT_LABEL = "cashback"
        private const val DISCOUNT_EVENT_LABEL = "diskon"
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
    private val selectedProducts by lazy {
        arguments?.getParcelableArrayList<SelectedProduct>(BundleConstant.BUNDLE_KEY_SELECTED_PRODUCTS)
            .orEmpty()
    }

    // color
    private val colorTintBlack by lazy {
        context?.let {
            ContextCompat.getColor(
                it,
                com.tokopedia.unifyprinciples.R.color.Unify_Static_Black
            )
        }
    }
    private val colorTintGreen by lazy {
        context?.let {
            ContextCompat.getColor(
                it,
                com.tokopedia.unifyprinciples.R.color.Green_G500
            )
        }
    }

    // coachmark
    private val coachMark by lazy {
        context?.let {
            CoachMark2(it)
        }
    }

    // tracker
    @Inject
    lateinit var tracker: VoucherSettingTracker

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

    override fun onFragmentBackPressed(): Boolean {
        viewModel.processEvent(VoucherCreationStepThreeEvent.TapBackButton)
        return super.onFragmentBackPressed()
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
        renderPromoTypeChips(state.voucherConfiguration, state.isDiscountPromoTypeEnabled)
        renderTicker(state.isDiscountPromoTypeEnabled, state.tickers)
    }

    private fun renderPromoTypeChips(
        voucherConfiguration: VoucherConfiguration,
        isDiscountPromoTypeEnabled: Boolean
    ) {
        when (voucherConfiguration.promoType) {
            PromoType.FREE_SHIPPING -> setFreeShippingSelected(isDiscountPromoTypeEnabled)
            PromoType.CASHBACK -> setCashbackSelected(isDiscountPromoTypeEnabled)
            PromoType.DISCOUNT -> {
                if (isDiscountPromoTypeEnabled) {
                    setDiscountSelected()
                }
            }
        }

        promoTypeSectionBinding?.chipDiscount?.showNewNotification = isDiscountPromoTypeEnabled

        if (!isDiscountPromoTypeEnabled) {
            promoTypeSectionBinding?.chipDiscount?.disable()
        }
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
            else -> {
                // no-op
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
            else -> {
                // no-op
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
                renderDiscountNominalInputValidation(state.isNominalError, state.nominalErrorMsg)
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
            else -> {
                // no-op
            }
        }
    }

    private fun handleAction(action: VoucherCreationStepThreeAction) {
        when (action) {
            is VoucherCreationStepThreeAction.BackToPreviousStep -> backToPreviousStep(action.voucherConfiguration)
            is VoucherCreationStepThreeAction.ContinueToNextStep -> continueToNextStep(action.voucherConfiguration)
            is VoucherCreationStepThreeAction.ShowCoachmark -> showCoachmark()
            is VoucherCreationStepThreeAction.ShowError -> {}
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

            setupCashbackNominalSection()
            setupCashbackPercentageSection()
            setupDiscountNominalSection()
            setupDiscountPercentageSection()

            setupTargetBuyerSection()
            setupSpendingEstimationSection()
            setupButtonSection()
            hideTextFieldLabel()

            setFreeShippingNominalInput()
            setFreeShippingMinimumBuyInput()
            setFreeShippingQuotaInput()

            setCashbackMaxDeductionInput()
            setCashbackMinimumBuyInput()
            setCashbackQuotaInput()

            setDiscountMaxDeductionInput()
            setDiscountMinimumBuyInput()
            setDiscountQuotaInput()

            registerCashbackPromoTypeSwitchListener()
            registerDiscountPromoTypeSwitchListener()
            presetValue()
        }
    }

    private fun registerDiscountPromoTypeSwitchListener() {
        discountInputSectionBinding?.switchPriceDiscount?.setOnCheckedChangeListener { _, isOn ->
            if (isOn) {
                setDiscountPercentageInput()
                viewModel.processEvent(
                    VoucherCreationStepThreeEvent.ChooseBenefitType(
                        BenefitType.PERCENTAGE
                    )
                )
                tracker.sendClickTipePotonganEvent(BenefitType.PERCENTAGE, voucherConfiguration.voucherId)
            } else {
                setDiscountNominalInput()
                viewModel.processEvent(
                    VoucherCreationStepThreeEvent.ChooseBenefitType(
                        BenefitType.NOMINAL
                    )
                )
                tracker.sendClickTipePotonganEvent(BenefitType.NOMINAL, voucherConfiguration.voucherId)
            }
        }
    }

    private fun registerCashbackPromoTypeSwitchListener() {
        cashbackInputSectionBinding?.switchPriceCashback?.setOnCheckedChangeListener { _, isOn ->
            if (isOn) {
                setCashbackPercentageInput()
                viewModel.processEvent(
                    VoucherCreationStepThreeEvent.ChooseBenefitType(
                        BenefitType.PERCENTAGE
                    )
                )
                tracker.sendClickTipePotonganEvent(BenefitType.PERCENTAGE, voucherConfiguration.voucherId)
            } else {
                setCashbackNominalInput()
                viewModel.processEvent(
                    VoucherCreationStepThreeEvent.ChooseBenefitType(
                        BenefitType.NOMINAL
                    )
                )
                tracker.sendClickTipePotonganEvent(BenefitType.NOMINAL, voucherConfiguration.voucherId)
            }
        }
    }

    private fun presetValue() {
        val currentVoucherConfiguration = viewModel.getCurrentVoucherConfiguration()
        freeShippingInputSectionBinding?.run {
            if (currentVoucherConfiguration.benefitIdr.isMoreThanZero()) {
                tfFreeShippingNominal.editText.setText(currentVoucherConfiguration.benefitIdr.toString())
            }
            if (currentVoucherConfiguration.minPurchase.isMoreThanZero()) {
                tfFreeShippingMinimumBuy.editText.setText(currentVoucherConfiguration.minPurchase.toString())
            }
            if (currentVoucherConfiguration.quota.isMoreThanZero()) {
                tfFreeShippingQuota.editText.setText(currentVoucherConfiguration.quota.toString())
            }
        }
        cashbackInputSectionBinding?.run {
            if (currentVoucherConfiguration.benefitIdr.isMoreThanZero()) {
                tfCashbackNominal.editText.setText(currentVoucherConfiguration.benefitIdr.toString())
            }
            if (currentVoucherConfiguration.benefitPercent.isMoreThanZero()) {
                tfCashbackPercentage.editText.setText(currentVoucherConfiguration.benefitPercent.toString())
            }
            if (currentVoucherConfiguration.benefitMax.isMoreThanZero()) {
                tfCahsbackMaxDeduction.editText.setText(currentVoucherConfiguration.benefitMax.toString())
            }
            if (currentVoucherConfiguration.minPurchase.isMoreThanZero()) {
                tfCashbackMinimumBuy.editText.setText(currentVoucherConfiguration.minPurchase.toString())
            }
            if (currentVoucherConfiguration.quota.isMoreThanZero()) {
                tfCashbackQuota.editText.setText(currentVoucherConfiguration.quota.toString())
            }
        }
        discountInputSectionBinding?.run {
            if (currentVoucherConfiguration.benefitIdr.isMoreThanZero()) {
                tfDiscountNominal.editText.setText(currentVoucherConfiguration.benefitIdr.toString())
            }
            if (currentVoucherConfiguration.benefitPercent.isMoreThanZero()) {
                tfDiscountPercentage.editText.setText(currentVoucherConfiguration.benefitPercent.toString())
            }
            if (currentVoucherConfiguration.benefitMax.isMoreThanZero()) {
                tfDiscountMaxDeduction.editText.setText(currentVoucherConfiguration.benefitMax.toString())
            }
            if (currentVoucherConfiguration.minPurchase.isMoreThanZero()) {
                tfDiscountMinimumBuy.editText.setText(currentVoucherConfiguration.minPurchase.toString())
            }
            if (currentVoucherConfiguration.quota.isMoreThanZero()) {
                tfDiscountQuota.editText.setText(currentVoucherConfiguration.quota.toString())
            }
        }
    }

    private fun hideTextFieldLabel() {
        freeShippingInputSectionBinding?.apply {
            tfFreeShippingNominal.labelText.gone()
            tfFreeShippingMinimumBuy.labelText.gone()
            tfFreeShippingQuota.labelText.gone()
        }
        cashbackInputSectionBinding?.apply {
            tfCashbackNominal.labelText.gone()
            tfCashbackPercentage.labelText.gone()
            tfCahsbackMaxDeduction.labelText.gone()
            tfCashbackMinimumBuy.labelText.gone()
            tfCashbackQuota.labelText.gone()
        }
        discountInputSectionBinding?.apply {
            tfDiscountNominal.labelText.gone()
            tfDiscountPercentage.labelText.gone()
            tfDiscountMaxDeduction.labelText.gone()
            tfDiscountMinimumBuy.labelText.gone()
            tfDiscountQuota.labelText.gone()
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
                onFragmentBackPressed()
                tracker.sendClickKembaliArrowEvent(voucherConfiguration.voucherId)
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
        promoTypeSectionBinding?.run {
            if (pageMode == PageMode.CREATE) {
                chipFreeShipping.chip_container.setOnClickListener {
                    setPromoType(PromoType.FREE_SHIPPING)
                }
                chipCashback.chip_container.setOnClickListener {
                    setPromoType(PromoType.CASHBACK)
                }
                chipDiscount.chip_container.setOnClickListener {
                    setPromoType(PromoType.DISCOUNT)
                }
            }
        }
    }

    private fun setPromoType(promoType: PromoType) {
        viewModel.processEvent(VoucherCreationStepThreeEvent.ChoosePromoType(promoType))
        tracker.sendClickPromoTypeEvent(promoType, voucherConfiguration.voucherId)
    }

    // Free shipping input region
    private fun setFreeShippingSelected(isDiscountPromoTypeEnabled: Boolean) {
        promoTypeSectionBinding?.run {
            chipFreeShipping.setSelected()
            chipCashback.setNormal()
        }

        if (isDiscountPromoTypeEnabled) {
            promoTypeSectionBinding?.chipDiscount?.setNormal()
        }

        freeShippingInputSectionBinding?.parentFreeShipping?.visible()
        cashbackInputSectionBinding?.parentCashback?.gone()
        discountInputSectionBinding?.parentDiscount?.gone()

        viewModel.processEvent(
            VoucherCreationStepThreeEvent.ChooseBenefitType(
                BenefitType.NOMINAL
            )
        )
    }

    private fun setFreeShippingNominalInput() {
        freeShippingInputSectionBinding?.run {
            tfFreeShippingNominal.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) {
                        tracker.sendClickFieldNominalCashbackEvent(
                            FREE_SHIPPING_EVENT_LABEL,
                            voucherConfiguration.voucherId
                        )
                    }
                }
                editText.setModeToNumberDelimitedInput(NOMINAL_INPUT_MAX_LENGTH)
                editText.textChangesAsFlow()
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputNominalChanged(it.digitsOnly())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setFreeShippingMinimumBuyInput() {
        freeShippingInputSectionBinding?.run {
            tfFreeShippingMinimumBuy.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) {
                        tracker.sendClickFieldMinimumCashbackEvent(
                            FREE_SHIPPING_EVENT_LABEL,
                            voucherConfiguration.voucherId
                        )
                    }
                }
                editText.setModeToNumberDelimitedInput(NOMINAL_INPUT_MAX_LENGTH)
                editText.textChangesAsFlow()
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(it.digitsOnly())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setFreeShippingQuotaInput() {
        freeShippingInputSectionBinding?.run {
            tfFreeShippingQuota.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) tracker.sendClickFieldQuotaCashbackEvent(FREE_SHIPPING_EVENT_LABEL, voucherConfiguration.voucherId)
                }
                editText.setModeToNumberDelimitedInput(QUOTA_INPUT_MAX_LENGTH)
                editText.textChangesAsFlow()
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputQuotaChanged(it.digitsOnly())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
            icInfo.setOnClickListener {
                QuotaInformationBottomSheet.newInstance().show(childFragmentManager)
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
    private fun setCashbackSelected(isDiscountPromoTypeEnabled: Boolean) {
        val currentVoucherConfiguration = viewModel.getCurrentVoucherConfiguration()
        promoTypeSectionBinding?.run {
            chipFreeShipping.setNormal()
            chipCashback.setSelected()
        }

        if (isDiscountPromoTypeEnabled) {
            promoTypeSectionBinding?.chipDiscount?.setNormal()
        }

        freeShippingInputSectionBinding?.parentFreeShipping?.gone()
        cashbackInputSectionBinding?.parentCashback?.visible()
        discountInputSectionBinding?.parentDiscount?.gone()

        setCashbackSwitchPriceInput(currentVoucherConfiguration)
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
        }
    }

    private fun setCashbackNominalInput() {
        cashbackInputSectionBinding?.tfCashbackNominal?.visible()
        cashbackInputSectionBinding?.tpgCashbackMaxDeductionLabel?.gone()
        cashbackInputSectionBinding?.tfCahsbackMaxDeduction?.gone()
        cashbackInputSectionBinding?.tfCashbackPercentage?.invisible()
    }

    private fun setupCashbackNominalSection() {
        cashbackInputSectionBinding?.run {
            tfCashbackNominal.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) tracker.sendClickFieldNominalCashbackEvent(CASHBACK_EVENT_LABEL, voucherConfiguration.voucherId)
                }
                appendText("")
                prependText(getString(R.string.smvc_rupiah_label))
                labelText.text = getString(R.string.smvc_nominal_cashback_label)
                editText.setModeToNumberDelimitedInput(NOMINAL_INPUT_MAX_LENGTH)
                editText.textChangesAsFlow()
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputNominalChanged(it.digitsOnly())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setupCashbackPercentageSection() {
        cashbackInputSectionBinding?.run {
            tfCashbackPercentage.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) tracker.sendClickFieldPersentaseCashbackEvent(CASHBACK_EVENT_LABEL, voucherConfiguration.voucherId)
                }
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) tracker.sendClickFieldPersentaseCashbackEvent(CASHBACK_EVENT_LABEL, voucherConfiguration.voucherId)
                }
                appendText(getString(R.string.smvc_percent_symbol))
                prependText("")
                labelText.text = getString(R.string.smvc_percentage_label)
                editText.setModeToNumberDelimitedInput(PERCENTAGE_INPUT_MAX_LENGTH)
                editText.textChangesAsFlow()
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputPercentageChanged(it.digitsOnly())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setCashbackPercentageInput() {
        cashbackInputSectionBinding?.tfCashbackPercentage?.visible()
        cashbackInputSectionBinding?.tfCashbackNominal?.invisible()
        cashbackInputSectionBinding?.tpgCashbackMaxDeductionLabel?.visible()
        cashbackInputSectionBinding?.tfCahsbackMaxDeduction?.visible()
    }

    private fun setCashbackMaxDeductionInput() {
        cashbackInputSectionBinding?.run {
            tfCahsbackMaxDeduction.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) tracker.sendClickFieldMaximumCashbackEvent(CASHBACK_EVENT_LABEL, voucherConfiguration.voucherId)
                }
                editText.setModeToNumberDelimitedInput(NOMINAL_INPUT_MAX_LENGTH)
                editText.textChangesAsFlow()
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMaxDeductionChanged(it.digitsOnly())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setCashbackMinimumBuyInput() {
        cashbackInputSectionBinding?.run {
            tfCashbackMinimumBuy.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) tracker.sendClickFieldMinimumCashbackEvent(CASHBACK_EVENT_LABEL, voucherConfiguration.voucherId)
                }
                editText.setModeToNumberDelimitedInput(NOMINAL_INPUT_MAX_LENGTH)
                editText.textChangesAsFlow()
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(it.digitsOnly())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setCashbackQuotaInput() {
        cashbackInputSectionBinding?.run {
            tfCashbackQuota.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) tracker.sendClickFieldQuotaCashbackEvent(CASHBACK_EVENT_LABEL, voucherConfiguration.voucherId)
                }
                editText.setModeToNumberDelimitedInput(QUOTA_INPUT_MAX_LENGTH)
                editText.textChangesAsFlow()
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputQuotaChanged(it.digitsOnly())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
            icInfo.setOnClickListener {
                QuotaInformationBottomSheet.newInstance().show(childFragmentManager)
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
        promoTypeSectionBinding?.run {
            chipDiscount.setSelected()
            chipFreeShipping.setNormal()
            chipCashback.setNormal()
        }

        freeShippingInputSectionBinding?.parentFreeShipping?.gone()
        cashbackInputSectionBinding?.parentCashback?.gone()
        discountInputSectionBinding?.parentDiscount?.visible()

        val currentVoucherConfiguration = viewModel.getCurrentVoucherConfiguration()
        setDiscountSwitchPriceInput(currentVoucherConfiguration)
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
        }
    }

    private fun setDiscountNominalInput() {
        discountInputSectionBinding?.tfDiscountNominal?.visible()
        discountInputSectionBinding?.tfDiscountPercentage?.invisible()
        discountInputSectionBinding?.tpgDiscountMaxDeductionLabel?.gone()
        discountInputSectionBinding?.tfDiscountMaxDeduction?.gone()

        viewModel.handleVoucherInputValidation()
    }

    private fun setupDiscountNominalSection() {
        discountInputSectionBinding?.run {
            tfDiscountNominal.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) tracker.sendClickFieldNominalCashbackEvent(DISCOUNT_EVENT_LABEL, voucherConfiguration.voucherId)
                }
                appendText("")
                prependText(getString(R.string.smvc_rupiah_label))
                labelText.text = getString(R.string.smvc_nominal_discount_label)
                editText.setModeToNumberDelimitedInput(NOMINAL_INPUT_MAX_LENGTH)
                editText.textChangesAsFlow()
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputNominalChanged(it.digitsOnly())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setDiscountPercentageInput() {
        discountInputSectionBinding?.tfDiscountPercentage?.visible()
        discountInputSectionBinding?.tfDiscountNominal?.invisible()
        discountInputSectionBinding?.tpgDiscountMaxDeductionLabel?.visible()
        discountInputSectionBinding?.tfDiscountMaxDeduction?.visible()

        viewModel.handleVoucherInputValidation()
    }

    private fun setupDiscountPercentageSection() {
        discountInputSectionBinding?.run {
            tfDiscountPercentage.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) tracker.sendClickFieldPersentaseCashbackEvent(DISCOUNT_EVENT_LABEL, voucherConfiguration.voucherId)
                }
                appendText(getString(R.string.smvc_percent_symbol))
                prependText("")
                labelText.text = getString(R.string.smvc_percentage_label)
                editText.setModeToNumberDelimitedInput(PERCENTAGE_INPUT_MAX_LENGTH)
                editText.textChangesAsFlow()
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputPercentageChanged(it.digitsOnly())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setDiscountMaxDeductionInput() {
        discountInputSectionBinding?.run {
            tfDiscountMaxDeduction.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) tracker.sendClickFieldMaximumCashbackEvent(DISCOUNT_EVENT_LABEL, voucherConfiguration.voucherId)
                }
                editText.setModeToNumberDelimitedInput(NOMINAL_INPUT_MAX_LENGTH)
                editText.textChangesAsFlow()
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMaxDeductionChanged(it.digitsOnly())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setDiscountMinimumBuyInput() {
        discountInputSectionBinding?.run {
            tfDiscountMinimumBuy.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) tracker.sendClickFieldMinimumCashbackEvent(DISCOUNT_EVENT_LABEL, voucherConfiguration.voucherId)
                }
                editText.setModeToNumberDelimitedInput(NOMINAL_INPUT_MAX_LENGTH)
                editText.textChangesAsFlow()
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputMinimumBuyChanged(it.digitsOnly())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
        }
    }

    private fun setDiscountQuotaInput() {
        discountInputSectionBinding?.run {
            tfDiscountQuota.apply {
                editText.setOnFocusChangeListener { _, isFocus ->
                    if (isFocus) tracker.sendClickFieldQuotaCashbackEvent(DISCOUNT_EVENT_LABEL, voucherConfiguration.voucherId)
                }
                editText.setModeToNumberDelimitedInput(QUOTA_INPUT_MAX_LENGTH)
                editText.textChangesAsFlow()
                    .debounce(DEBOUNCE)
                    .distinctUntilChanged()
                    .onEach {
                        viewModel.processEvent(
                            VoucherCreationStepThreeEvent.OnInputQuotaChanged(it.digitsOnly())
                        )
                    }
                    .launchIn(lifecycleScope)
            }
            icInfo.setOnClickListener {
                QuotaInformationBottomSheet.newInstance().show(childFragmentManager)
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
            rgBuyerTarget.apply {
                setOnCheckedChangeListener { _, radioButtonId ->
                    val currentVoucherConfiguration = viewModel.getCurrentVoucherConfiguration()
                    val target = if (radioButtonId == radioAllBuyer.id) {
                        VoucherTargetBuyer.ALL_BUYER
                    } else {
                        VoucherTargetBuyer.NEW_FOLLOWER
                    }
                    viewModel.processEvent(VoucherCreationStepThreeEvent.ChooseTargetBuyer(target))
                    tracker.sendClickTargetPembeliEvent(
                        target,
                        currentVoucherConfiguration.promoType,
                        voucherConfiguration.voucherId
                    )
                }
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
                for (targetBuyer in availableTargetBuyer) {
                    rgBuyerTarget.getChildAt(targetBuyer.id).enable()
                }
            }

            when (voucherConfiguration.targetBuyer) {
                VoucherTargetBuyer.ALL_BUYER -> {
                    if (pageMode == PageMode.CREATE) radioAllBuyer.enable()
                    radioAllBuyer.isChecked = true
                }
                VoucherTargetBuyer.NEW_FOLLOWER -> {
                    if (pageMode == PageMode.CREATE) radioNewFollower.enable()
                    radioNewFollower.isChecked = true
                }
                else -> {
                    if (pageMode == PageMode.CREATE) radioAllBuyer.enable()
                    radioAllBuyer.isChecked = true
                }
            }
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
            btnBack.setOnClickListener {
                viewModel.processEvent(VoucherCreationStepThreeEvent.TapBackButton)
                tracker.sendClickKembaliButtonEvent(voucherConfiguration.voucherId)
            }
            btnContinue.text = if (pageMode == PageMode.CREATE) {
                getString(R.string.smvc_continue)
            } else {
                getText(R.string.smvc_save)
            }
        }
    }

    private fun backToPreviousStep(currentVoucherConfiguration: VoucherConfiguration) {
        if (pageMode == PageMode.CREATE) {
            if (voucherConfiguration.isFinishedFillAllStep()) {
                navigateToVoucherSummaryPage(voucherConfiguration)
            } else {
                navigateToVoucherInfoPage(currentVoucherConfiguration)
            }
        } else {
            navigateToVoucherSummaryPage(voucherConfiguration)
        }
        activity?.finish()
    }

    private fun continueToNextStep(voucherConfiguration: VoucherConfiguration) {
        if (pageMode == PageMode.CREATE) {
            if (voucherConfiguration.isVoucherProduct) {
                if (voucherConfiguration.isFinishedFillAllStep()) {
                    navigateToVoucherSummaryPage(voucherConfiguration)
                } else {
                    navigateToAddProductPage(voucherConfiguration)
                }
            } else {
                navigateToVoucherSummaryPage(voucherConfiguration)
            }
        } else {
            navigateToVoucherSummaryPage(voucherConfiguration)
        }
    }

    private fun navigateToVoucherInfoPage(currentVoucherConfiguration: VoucherConfiguration) {
        context?.let { ctx ->
            VoucherInformationActivity.buildCreateModeIntent(
                ctx,
                currentVoucherConfiguration
            )
        }
    }

    private fun navigateToAddProductPage(currentVoucherConfiguration: VoucherConfiguration) {
        val intent = AddProductActivity.buildCreateModeIntent(
            context ?: return,
            currentVoucherConfiguration.copy(isFinishFilledStepThree = true)
        )
        startActivity(intent)
    }

    private fun navigateToVoucherSummaryPage(currentVoucherConfiguration: VoucherConfiguration) {
        context?.let { ctx ->
            SummaryActivity.start(
                ctx,
                currentVoucherConfiguration.copy(isFinishFilledStepThree = true),
                selectedProducts
            )
        }
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
                    tracker.sendClickLanjutEvent(voucherConfiguration.voucherId)
                }
            }
        }
    }

    private fun ChipsUnify.setNormal() {
        this.apply {
            chipType = ChipsUnify.TYPE_NORMAL
            colorTintBlack?.let { color -> chip_image_icon.setColorFilter(color) }
        }
    }

    private fun ChipsUnify.setSelected() {
        this.apply {
            chipType = ChipsUnify.TYPE_SELECTED
            colorTintGreen?.let { color -> chip_image_icon.setColorFilter(color) }
        }
    }

    private fun ChipsUnify.disable() {
        val color = ContextCompat.getColor(
            context,
            com.tokopedia.unifyprinciples.R.color.Unify_NN400
        )
        chipType = ChipsUnify.TYPE_DISABLE
        chip_image_icon.setColorFilter(color)
    }

    private fun renderTicker(isDiscountPromoTypeEnabled: Boolean, tickers: List<RemoteTicker>) {
        if (!isDiscountPromoTypeEnabled && tickers.isNotEmpty()) {
            showTickers(tickers)
        }
    }

    private fun showTickers(tickers: List<RemoteTicker>) {
        binding?.run {
            ticker.visible()

            val remoteTickers = tickers.map { remoteTicker ->
                TickerData(
                    title = remoteTicker.title,
                    description = remoteTicker.description,
                    isFromHtml = true,
                    type = Ticker.TYPE_ANNOUNCEMENT
                )
            }

            val tickerAdapter = TickerPagerAdapter(activity ?: return, remoteTickers)
            tickerAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    routeToUrl(linkUrl.toString())
                }
            })

            ticker.addPagerView(tickerAdapter, remoteTickers)
            ticker.setDescriptionClickEvent(object : TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {
                    routeToUrl(linkUrl.toString())
                }

                override fun onDismiss() {
                }
            })
        }
    }
}
