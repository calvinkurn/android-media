package com.tokopedia.vouchercreation.create.view.fragment.vouchertype

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.utils.showErrorToaster
import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationUiModel
import com.tokopedia.vouchercreation.common.view.textfield.vouchertype.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.create.data.source.PromotionTypeUiListStaticDataSource
import com.tokopedia.vouchercreation.create.view.enums.CashbackType
import com.tokopedia.vouchercreation.create.view.enums.PromotionType
import com.tokopedia.vouchercreation.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.CashbackExpenseInfoBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.GeneralExpensesInfoBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeItemAdapterFactory
import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.*
import com.tokopedia.vouchercreation.create.view.viewmodel.CashbackVoucherCreateViewModel
import javax.inject.Inject

class CashbackVoucherCreateFragment : BaseListFragment<Visitable<*>, PromotionTypeItemAdapterFactory>() {

    companion object {@JvmStatic
        fun createInstance(onNextStep: (VoucherImageType, Int, Int) -> Unit,
                           onShouldChangeBannerValue: (VoucherImageType) -> Unit,
                           context: Context,
                           getVoucherReviewUiModel: () -> VoucherReviewUiModel?) = CashbackVoucherCreateFragment().apply {
            this.onNextStep = onNextStep
            this.onShouldChangeBannerValue = onShouldChangeBannerValue
            viewContext = context
            this.getVoucherReviewUiModel = getVoucherReviewUiModel

            getVoucherReviewUiModel()?.run {
                voucherImageType = voucherType
                activeCashbackType =
                        when(voucherType) {
                            is VoucherImageType.Rupiah -> CashbackType.Rupiah
                            is VoucherImageType.Percentage -> CashbackType.Percentage
                            else -> CashbackType.Rupiah
                        }
            }
        }

        private const val INPUT_FIELD_ADAPTER_SIZE = 1

        private const val TICKER_INDEX_POSITION = 0

        private const val ERROR_MESSAGE = "Error validate cashback voucher"
    }

    private var onNextStep: (VoucherImageType, Int, Int) -> Unit = { _,_,_ -> }
    private var onShouldChangeBannerValue: (VoucherImageType) -> Unit = { _ -> }
    private var viewContext = context
    private var getVoucherReviewUiModel: () -> VoucherReviewUiModel? = { null }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(CashbackVoucherCreateViewModel::class.java)
    }

    private val voucherTitleUiModel by lazy {
        VoucherTitleUiModel(context?.getString(R.string.mvc_create_cashback).toBlankOrString())
    }

    // Commented temporary because of product requirement
//    private val promoDescTickerModel by lazy {
//        PromotionTypeTickerUiModel(R.string.mvc_create_promo_type_cashback_ticker, ::onDismissTicker)
//    }

    private val unavailableTickerUiModel by lazy {
        UnavailableTickerUiModel(::onCloseTicker)
    }

    private val cashbackTypePickerModel by lazy {
        CashbackTypePickerUiModel(::onCashbackSelectedType, activeCashbackType)
    }

    private val expensesInfoBottomSheetFragment by lazy {
        GeneralExpensesInfoBottomSheetFragment.createInstance(viewContext).apply {
            setTitle(bottomSheetViewTitle.toBlankOrString())
        }
    }

    private val percentageExpenseBottomSheet by lazy {
        viewContext?.let {
            CashbackExpenseInfoBottomSheetFragment.createInstance(it, ::getCashbackInfo).apply {
                setOnDismissListener {
                    adapter.run {
                        notifyItemChanged(dataSize - 1)
                    }
                }
                setEditNowButtonClicked {
                    VoucherCreationTracking.sendCreateVoucherClickTracking(
                            step = VoucherCreationStep.BENEFIT,
                            action = VoucherCreationAnalyticConstant.EventAction.Click.PRICE_SUGGESTION_EDIT_NOW,
                            userId = userSession.userId
                    )
                }
            }}
    }

    private val rupiahMaximumDiscountTextFieldModel by lazy {
        PromotionTypeUiListStaticDataSource.getCashbackMaximumDiscountTextFieldUiModel(::onTextFieldValueChanged, ::onSetErrorMessage, PromotionType.Cashback.Rupiah.MaximumDiscount).apply {
            getVoucherReviewUiModel()?.let { data ->
                if (data.voucherType is VoucherImageType.Rupiah) {
                    currentValue = data.voucherType.value
                }
            }
        }
    }

    private val rupiahMinimumPurchaseTextFieldModel by lazy {
        PromotionTypeUiListStaticDataSource.getMinimumPurchaseTextFieldUiModel(::onTextFieldValueChanged, ::onSetErrorMessage, PromotionType.Cashback.Rupiah.MinimumPurchase).apply {
            getVoucherReviewUiModel()?.let { data ->
                if (data.voucherType is VoucherImageType.Rupiah) {
                    currentValue = data.minPurchase
                }
            }
        }
    }

    private val rupiahVoucherQuotaTextFieldModel by lazy {
        PromotionTypeUiListStaticDataSource.getVoucherQuotaTextFieldUiModel(::onTextFieldValueChanged, ::onSetErrorMessage, PromotionType.Cashback.Rupiah.VoucherQuota).apply {
            getVoucherReviewUiModel()?.let { data ->
                if (data.voucherType is VoucherImageType.Rupiah) {
                    currentValue = data.voucherQuota
                }
            }
        }
    }

    private val discountAmountTextFieldModel by lazy {
        PromotionTypeUiListStaticDataSource.getCashbackPercentageDiscountAmountTextFieldUiModel(::onTextFieldValueChanged, ::onSetErrorMessage).apply {
            getVoucherReviewUiModel()?.let { data ->
                (data.voucherType as? VoucherImageType.Percentage)?.percentage?.let {
                    currentValue = it
                }
            }
        }
    }

    private val percentageMaximumDiscountTextFieldModel by lazy {
        PromotionTypeUiListStaticDataSource.getCashbackMaximumDiscountTextFieldUiModel(::onTextFieldValueChanged, ::onSetErrorMessage, PromotionType.Cashback.Percentage.MaximumDiscount).apply {
            getVoucherReviewUiModel()?.let { data ->
                if (data.voucherType is VoucherImageType.Percentage) {
                    currentValue = data.voucherType.value
                }
            }
        }
    }

    private val percentageMinimumPurchaseTextFieldModel by lazy {
        PromotionTypeUiListStaticDataSource.getMinimumPurchaseTextFieldUiModel(::onTextFieldValueChanged, ::onSetErrorMessage, PromotionType.Cashback.Percentage.MinimumPurchase).apply {
            getVoucherReviewUiModel()?.let { data ->
                if (data.voucherType is VoucherImageType.Percentage) {
                    currentValue = data.minPurchase
                }
            }
        }
    }

    private val percentageVoucherQuotaTextFieldModel by lazy {
        PromotionTypeUiListStaticDataSource.getVoucherQuotaTextFieldUiModel(::onTextFieldValueChanged, ::onSetErrorMessage, PromotionType.Cashback.Percentage.VoucherQuota).apply {
            getVoucherReviewUiModel()?.let { data ->
                if (data.voucherType is VoucherImageType.Percentage) {
                    currentValue = data.voucherQuota
                }
            }
        }
    }


    private val promotionExpenseEstimationUiModel by lazy {
        PromotionExpenseEstimationUiModel(
                onTooltipClicked = ::onTooltipClicked
        )
    }

    private val nextButtonUiModel by lazy {
        NextButtonUiModel(::onNext)
    }

    private val topSectionUiModelList by lazy {
        listOf(
                voucherTitleUiModel,
                unavailableTickerUiModel,
                cashbackTypePickerModel
        )
    }

    private val rupiahCashbackTextFieldList by lazy {
        listOf(
                rupiahMaximumDiscountTextFieldModel,
                rupiahMinimumPurchaseTextFieldModel,
                rupiahVoucherQuotaTextFieldModel
        )
    }

    private val rupiahCashbackAdapterUiModel by lazy {
        rupiahCashbackTextFieldList.run {
            if (::viewModelFactory.isInitialized) {
                forEach { uiModel ->
                    viewModel.addTextFieldValueToCalculation(uiModel.currentValue, uiModel.promotionType)
                }
            }
            PromotionTypeInputListUiModel(this)
        }
    }

    private val percentageCashbackTextFieldList by lazy {
        listOf(
                discountAmountTextFieldModel,
                percentageMaximumDiscountTextFieldModel,
                percentageMinimumPurchaseTextFieldModel,
                percentageVoucherQuotaTextFieldModel
        )
    }

    private val percentageCashbackAdapterUiModel by lazy {
        percentageCashbackTextFieldList.run {
            if (::viewModelFactory.isInitialized) {
                forEach { uiModel ->
                    viewModel.addTextFieldValueToCalculation(uiModel.currentValue, uiModel.promotionType)
                }
            }
            PromotionTypeInputListUiModel(this)
        }
    }

    private val bottomSectionUiModelList by lazy {
        listOf(
                promotionExpenseEstimationUiModel,
                nextButtonUiModel
        )
    }

    private val initialTextFieldIndex by lazy {
        topSectionUiModelList.size - 1
    }

    private var cashbackPercentageInfoUiModel =
            CashbackPercentageInfoUiModel(0,0,0)

    private var activeCashbackType: CashbackType = CashbackType.Rupiah

    private var voucherImageType: VoucherImageType = VoucherImageType.Rupiah(0)

    private var isRupiahWaitingForValidation = false
    private var isPercentageWaitingForValidation = false

    private var textFieldIndex = 0

    override fun getAdapterTypeFactory(): PromotionTypeItemAdapterFactory = PromotionTypeItemAdapterFactory()

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {}

    override fun onResume() {
        super.onResume()

        viewModel.refreshValue()
        cashbackTypePickerModel.currentActiveType = activeCashbackType
        // We actually don't need to notify adapter data as the data has not been changed
        // But, as we used view pager and each fragment has different height (which will cut some layout when page changes according to previous page),
        // we will notify the adapter to mimic layout refresh
        adapter.run {
            notifyItemChanged(data.indexOf(cashbackTypePickerModel))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeLiveData()
        return inflater.inflate(R.layout.fragment_voucher_promotion_type, container, false)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvMvcVoucherType

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        textFieldIndex = initialTextFieldIndex
        renderList(getCashbackTypeUiList())
        viewModel.changeCashbackType(activeCashbackType)
    }

    private fun observeLiveData() {
        viewLifecycleOwner.run {
            observe(viewModel.expenseEstimationLiveData) { value ->
                promotionExpenseEstimationUiModel.estimationValue = value
                adapter.run {
                    notifyItemChanged(data.indexOf(promotionExpenseEstimationUiModel))
                }
            }
            observe(viewModel.rupiahValueList) { valueList ->
                rupiahCashbackTextFieldList.forEachIndexed { index, voucherTextFieldUiModel ->
                    voucherTextFieldUiModel.currentValue = valueList[index]
                }
            }
            observe(viewModel.percentageValueList) { valueList ->
                percentageCashbackTextFieldList.forEachIndexed { index, voucherTextFieldUiModel ->
                    voucherTextFieldUiModel.currentValue = valueList[index]
                }
            }
            observe(viewModel.rupiahErrorPairList) { errorPairList ->
                rupiahCashbackTextFieldList.forEachIndexed { index, voucherTextFieldUiModel ->
                    voucherTextFieldUiModel.currentErrorPair = errorPairList[index]
                }
            }
            observe(viewModel.percentageErrorPairList) { errorPairList ->
                percentageCashbackTextFieldList.forEachIndexed { index, voucherTextFieldUiModel ->
                    voucherTextFieldUiModel.currentErrorPair = errorPairList[index]
                }
            }
            observe(viewModel.cashbackPercentageInfoUiModelLiveData) { uiModel ->
                cashbackPercentageInfoUiModel = uiModel
            }
            observe(viewModel.cashbackTypeData) { type ->
                activeCashbackType = type
                adapter.data.run {
                    (get(indexOf(cashbackTypePickerModel)) as? CashbackTypePickerUiModel)?.currentActiveType = activeCashbackType
                }
            }
            observe(viewModel.voucherImageValueLiveData) { imageValue ->
                voucherImageType = imageValue
                onShouldChangeBannerValue(imageValue)
            }
            observe(viewModel.rupiahValidationLiveData) { result ->
                if (isRupiahWaitingForValidation) {
                    viewModel.refreshValue()
                    when(result) {
                        is Success -> {
                            val validation = result.data
                            if (!validation.getIsHaveError()) {
                                activity?.run {
                                    KeyboardHandler.hideSoftKeyboard(this)
                                }
                                onNextStep(voucherImageType, getRupiahValue(rupiahMinimumPurchaseTextFieldModel), getRupiahValue(rupiahVoucherQuotaTextFieldModel))
                            } else {
                                validation.run {
                                    benefitMaxError.setRupiahTextFieldError(rupiahMaximumDiscountTextFieldModel)
                                    minPurchaseError.setRupiahTextFieldError(rupiahMinimumPurchaseTextFieldModel)
                                    quotaError.setRupiahTextFieldError(rupiahVoucherQuotaTextFieldModel)
                                    benefitTypeError.let { error ->
                                        if (error.isNotBlank()) {
                                            view?.showErrorToaster(error)
                                            return@observe
                                        }
                                    }
                                    couponTypeError.let { error ->
                                        if (error.isNotBlank()) {
                                            view?.showErrorToaster(error)
                                            return@observe
                                        }
                                    }
                                }
                            }
                        }
                        is Fail -> {
                            val error = result.throwable.message.toBlankOrString()
                            view?.showErrorToaster(error)
                            MvcErrorHandler.logToCrashlytics(result.throwable, ERROR_MESSAGE)
                        }
                    }
                    adapter.notifyDataSetChanged()
                    isRupiahWaitingForValidation = false
                }
            }
            observe(viewModel.percentageValidationLiveData) { result ->
                if (isPercentageWaitingForValidation) {
                    viewModel.refreshValue()
                    when(result) {
                        is Success -> {
                            val validation = result.data
                            if (!validation.getIsHaveError()) {
                                activity?.run {
                                    KeyboardHandler.hideSoftKeyboard(this)
                                }
                                onNextStep(voucherImageType, getPercentageValue(percentageMinimumPurchaseTextFieldModel), getPercentageValue(percentageVoucherQuotaTextFieldModel))
                            } else {
                                validation.run {
                                    benefitPercentError.setPercentageTextFieldError(discountAmountTextFieldModel)
                                    benefitMaxError.setPercentageTextFieldError(percentageMaximumDiscountTextFieldModel)
                                    minPurchaseError.setPercentageTextFieldError(percentageMinimumPurchaseTextFieldModel)
                                    quotaError.setPercentageTextFieldError(percentageVoucherQuotaTextFieldModel)
                                    benefitTypeError.let { error ->
                                        if (error.isNotBlank()) {
                                            view?.showErrorToaster(error)
                                            return@observe
                                        }
                                    }
                                    couponTypeError.let { error ->
                                        if (error.isNotBlank()) {
                                            view?.showErrorToaster(error)
                                            return@observe
                                        }
                                    }
                                }
                            }
                        }
                        is Fail -> {
                            val error = result.throwable.message.toBlankOrString()
                            view?.showErrorToaster(error)
                            MvcErrorHandler.logToCrashlytics(result.throwable, ERROR_MESSAGE)
                        }
                    }
                    adapter.notifyDataSetChanged()
                    isPercentageWaitingForValidation = false
                }
            }
        }
    }

    private fun getCashbackTypeUiList() : List<Visitable<*>> {
        return mutableListOf<Visitable<*>>().apply {
            addAll(topSectionUiModelList)
            if (activeCashbackType is CashbackType.Rupiah) {
                add(rupiahCashbackAdapterUiModel)
            } else {
                add(percentageCashbackAdapterUiModel)
            }
            addAll(bottomSectionUiModelList)
        }
    }

    private fun onDismissTicker() {
        adapter.data.remove(unavailableTickerUiModel)
        adapter.notifyItemRemoved(TICKER_INDEX_POSITION)
    }

    private fun onNext() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.BENEFIT,
                action = VoucherCreationAnalyticConstant.EventAction.Click.CONTINUE,
                label = VoucherCreationAnalyticConstant.EventLabel.CASHBACK,
                userId = userSession.userId
        )
        if (activeCashbackType is CashbackType.Percentage) {
            if (checkIfValuesAreCorrect(cashbackPercentageInfoUiModel.minimumDiscount, cashbackPercentageInfoUiModel.maximumDiscount)) {
                validatePercentageValues()
            } else {
                activity?.run {
                    KeyboardHandler.hideSoftKeyboard(this)
                }
                percentageExpenseBottomSheet?.show(childFragmentManager, CashbackExpenseInfoBottomSheetFragment::class.java.name)
            }
        } else {
            validateRupiahValues()
        }
    }

    /**
     * We need to know the index of text input fields inside the adapter as the layout size is volatile (ticker could be closed)
     * The text field index is equal to total visitables adapter size minus the bottom section model size and the input field itself
     */
    private fun onCashbackSelectedType(cashbackType: CashbackType) {
        val extraSize = INPUT_FIELD_ADAPTER_SIZE + bottomSectionUiModelList.size
        val cashbackTypeEventAction: String
        textFieldIndex = adapter.data.size - extraSize
        adapter.data.removeAt(textFieldIndex)
        when(cashbackType) {
            CashbackType.Rupiah -> {
                adapter.data.add(textFieldIndex, rupiahCashbackAdapterUiModel)
                cashbackTypeEventAction = VoucherCreationAnalyticConstant.EventAction.Click.CASHBACK_TYPE_RUPIAH
            }
            CashbackType.Percentage -> {
                adapter.data.add(textFieldIndex, percentageCashbackAdapterUiModel)
                cashbackTypeEventAction = VoucherCreationAnalyticConstant.EventAction.Click.CASHBACK_TYPE_PERCENTAGE
            }
        }

        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.BENEFIT,
                action = cashbackTypeEventAction,
                userId = userSession.userId
        )
        viewModel.changeCashbackType(cashbackType)

        adapter.notifyItemChanged(textFieldIndex)
    }

    private fun onTextFieldValueChanged(value: Int?, type: PromotionType) {
        (type as? PromotionType.Cashback)?.let {
            viewModel.addTextFieldValueToCalculation(value, it)
        }
    }

    private fun onSetErrorMessage(isError: Boolean, errorMessage: String?, type: PromotionType) {
        (type as? PromotionType.Cashback)?.let {
            viewModel.addErrorPair(isError, errorMessage.toBlankOrString(), it)
        }
    }

    private fun onTooltipClicked() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.BENEFIT,
                action = VoucherCreationAnalyticConstant.EventAction.Click.TOOLTIP_SPENDING_ESTIMATION,
                userId = userSession.userId
        )
        activity?.run {
            KeyboardHandler.hideSoftKeyboard(this)
        }
        expensesInfoBottomSheetFragment.show(childFragmentManager, GeneralExpensesInfoBottomSheetFragment::class.java.name)
    }

    private fun onCloseTicker() {
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.BENEFIT,
                action = VoucherCreationAnalyticConstant.EventAction.Click.CLOSE_INFO_SECTION,
                userId = userSession.userId
        )
    }

    private fun getCashbackInfo(): CashbackPercentageInfoUiModel = cashbackPercentageInfoUiModel

    private fun checkIfValuesAreCorrect(minimumDiscount: Int, maximumDiscount: Int): Boolean =
            minimumDiscount < maximumDiscount

    private fun validateRupiahValues() {
        isRupiahWaitingForValidation = true
        viewModel.validateCashbackRupiahValues()
    }

    private fun validatePercentageValues() {
        isPercentageWaitingForValidation = true
        viewModel.validateCashbackPercentageValues()
    }

    private fun String.setRupiahTextFieldError(voucherTextFieldUiModel: VoucherTextFieldUiModel) {
        if (isNotBlank()) {
            rupiahCashbackTextFieldList.run {
                get(indexOf(voucherTextFieldUiModel)).currentErrorPair = Pair(true, this@setRupiahTextFieldError)
            }
        }
    }

    private fun String.setPercentageTextFieldError(voucherTextFieldUiModel: VoucherTextFieldUiModel) {
        if (isNotBlank()) {
            percentageCashbackTextFieldList.run {
                get(indexOf(voucherTextFieldUiModel)).currentErrorPair = Pair(true, this@setPercentageTextFieldError)
            }
        }
    }

    private fun getRupiahValue(voucherTextFieldUiModel: VoucherTextFieldUiModel) =
            with(rupiahCashbackTextFieldList) {
                get(indexOf(voucherTextFieldUiModel)).currentValue.toZeroIfNull()
            }

    private fun getPercentageValue(voucherTextFieldUiModel: VoucherTextFieldUiModel) =
            with(percentageCashbackTextFieldList) {
                get(indexOf(voucherTextFieldUiModel)).currentValue.toZeroIfNull()
            }

}

