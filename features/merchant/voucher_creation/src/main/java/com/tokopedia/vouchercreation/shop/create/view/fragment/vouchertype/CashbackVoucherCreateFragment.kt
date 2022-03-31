package com.tokopedia.vouchercreation.shop.create.view.fragment.vouchertype

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
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.consts.VoucherRecommendationStatus
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.utils.dismissBottomSheetWithTags
import com.tokopedia.vouchercreation.common.utils.showErrorToaster
import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationUiModel
import com.tokopedia.vouchercreation.common.view.textfield.vouchertype.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.shop.create.data.source.PromotionTypeUiListStaticDataSource
import com.tokopedia.vouchercreation.shop.create.domain.model.VoucherRecommendationData
import com.tokopedia.vouchercreation.shop.create.view.enums.CashbackType
import com.tokopedia.vouchercreation.shop.create.view.enums.PromotionType
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherCreationStep
import com.tokopedia.vouchercreation.shop.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.shop.create.view.fragment.bottomsheet.CashbackExpenseInfoBottomSheetFragment
import com.tokopedia.vouchercreation.shop.create.view.fragment.bottomsheet.GeneralExpensesInfoBottomSheetFragment
import com.tokopedia.vouchercreation.shop.create.view.typefactory.vouchertype.PromotionTypeItemAdapterFactory
import com.tokopedia.vouchercreation.shop.create.view.uimodel.NextButtonUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.shop.create.view.uimodel.vouchertype.item.*
import com.tokopedia.vouchercreation.shop.create.view.viewholder.vouchertype.item.RecommendationTickerViewHolder
import com.tokopedia.vouchercreation.shop.create.view.viewmodel.CashbackVoucherCreateViewModel
import javax.inject.Inject

class CashbackVoucherCreateFragment : BaseListFragment<Visitable<*>, PromotionTypeItemAdapterFactory>(), RecommendationTickerViewHolder.OnClickableSpanClickedListener {

    companion object {
        @JvmStatic
        fun createInstance(onNextStep: (VoucherImageType, Int, Int) -> Unit,
                           setRecommendationStatus: (Int) -> Unit,
                           onShouldChangeBannerValue: (VoucherImageType) -> Unit,
                           context: Context,
                           getVoucherReviewUiModel: () -> VoucherReviewUiModel?,
                           isCreateNew: Boolean) = CashbackVoucherCreateFragment().apply {
            this.onNextStep = onNextStep
            this.setRecommendationStatus = setRecommendationStatus
            this.onShouldChangeBannerValue = onShouldChangeBannerValue
            viewContext = context
            this.getVoucherReviewUiModel = getVoucherReviewUiModel
            this.isCreateNew = isCreateNew

            getVoucherReviewUiModel()?.run {
                voucherImageType = voucherType
                activeCashbackType =
                        when (voucherType) {
                            is VoucherImageType.Rupiah -> CashbackType.Rupiah
                            is VoucherImageType.Percentage -> CashbackType.Percentage
                            else -> CashbackType.Rupiah
                        }
            }
        }
        private const val INPUT_FIELD_ADAPTER_SIZE = 1
        private const val VALIDATE_RUPIAH_INPUT_ERROR = "Validate rupiah input error"
        private const val VALIDATE_PERCENTAGE_INPUT_ERROR = "Validate rupiah input error"
        private const val GET_RUPIAH_VOUCHER_RECOMMENDATION_ERROR = "Get rupiah voucher recommendation error"
        private const val GET_PERCENTAGE_VOUCHER_RECOMMENDATION_ERROR = "Get percentage voucher recommendation error"
    }

    private var onNextStep: (VoucherImageType, Int, Int) -> Unit = { _, _, _ -> }
    private var setRecommendationStatus: (Int) -> Unit = { _ -> }
    private var onShouldChangeBannerValue: (VoucherImageType) -> Unit = { _ -> }
    private var viewContext = context
    private var getVoucherReviewUiModel: () -> VoucherReviewUiModel? = { null }
    private var isCreateNew: Boolean = true

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

    private val recommendationTickerUiModel by lazy {
        RecommendationTickerUiModel(true)
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
            }
        }
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
        NextButtonUiModel(::onNext, true)
    }

    private val topSectionUiModelList by lazy {
        listOf(
                recommendationTickerUiModel,
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
            CashbackPercentageInfoUiModel(0, 0, 0)

    private var activeCashbackType: CashbackType = CashbackType.Rupiah

    private var voucherImageType: VoucherImageType = VoucherImageType.Rupiah(0)

    private var isRupiahWaitingForValidation = false
    private var isPercentageWaitingForValidation = false

    private var textFieldIndex = 0

    override fun getAdapterTypeFactory(): PromotionTypeItemAdapterFactory = PromotionTypeItemAdapterFactory(this)

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

    override fun onPause() {
        super.onPause()
        childFragmentManager.dismissBottomSheetWithTags(
                CashbackExpenseInfoBottomSheetFragment.TAG,
                GeneralExpensesInfoBottomSheetFragment.TAG
        )
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvMvcVoucherType

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        viewModel.getVoucherRecommendationFromApi()
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
                    when (result) {
                        is Success -> {
                            val validation = result.data
                            val isError = validation.getIsHaveError()
                            if (!isError) {
                                activity?.run {
                                    KeyboardHandler.hideSoftKeyboard(this)
                                }

                                val minPurchase = viewModel.getMinimumPurchase(PromotionType.Cashback.Rupiah.MinimumPurchase)
                                val quota = viewModel.getVoucherQuota(PromotionType.Cashback.Rupiah.VoucherQuota)

                                onNextStep(voucherImageType, minPurchase, quota)
                                setRecommendationStatus(viewModel.voucherRecommendationStatus.value
                                        ?: VoucherRecommendationStatus.WITH_RECOMMENDATION)
                            } else {
                                validation.run {
                                    // show the max discount error and store the error state in the view model
                                    benefitMaxError.setRupiahTextFieldError(rupiahMaximumDiscountTextFieldModel)
                                    if (benefitMaxError.isNotBlank()) viewModel.addErrorPair(isError, benefitMaxError, PromotionType.Cashback.Rupiah.MaximumDiscount)
                                    // show the min purchase error and store the error state in the view model
                                    minPurchaseError.setRupiahTextFieldError(rupiahMinimumPurchaseTextFieldModel)
                                    if (minPurchaseError.isNotBlank()) viewModel.addErrorPair(isError, minPurchaseError, PromotionType.Cashback.Rupiah.MinimumPurchase)
                                    // show the voucher quota error and store the error state in the view model
                                    quotaError.setRupiahTextFieldError(rupiahVoucherQuotaTextFieldModel)
                                    if (quotaError.isNotBlank()) viewModel.addErrorPair(isError, quotaError, PromotionType.Cashback.Rupiah.VoucherQuota)

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
                            // disable the next button when an error occurred
                            nextButtonUiModel.isEnabled = !isError
                            // store the rupiah input error state to preserve the next button state
                            viewModel.isRupiahInputError = isError
                        }
                        is Fail -> {
                            // show user friendly error message to user
                            val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                            view?.showErrorToaster(errorMessage)
                            // send crash report to firebase crashlytics
                            MvcErrorHandler.logToCrashlytics(result.throwable, VALIDATE_RUPIAH_INPUT_ERROR)
                            // log error type to scalyr
                            ServerLogger.log(Priority.P2, "MVC_VALIDATE_RUPIAH_INPUT_ERROR", mapOf("type" to errorMessage))
                        }
                    }
                    // update the input list values for rupiah
                    viewModel.refreshValue()
                    adapter.notifyDataSetChanged()
                    isRupiahWaitingForValidation = false
                }
            }
            observe(viewModel.percentageValidationLiveData) { result ->
                if (isPercentageWaitingForValidation) {
                    viewModel.refreshValue()
                    when (result) {
                        is Success -> {
                            val validation = result.data
                            val isError = validation.getIsHaveError()
                            if (!isError) {
                                activity?.run {
                                    KeyboardHandler.hideSoftKeyboard(this)
                                }
                                val minPurchase = viewModel.getMinimumPurchase(PromotionType.Cashback.Percentage.MinimumPurchase)
                                val quota = viewModel.getVoucherQuota(PromotionType.Cashback.Percentage.VoucherQuota)
                                onNextStep(voucherImageType, minPurchase, quota)
                                setRecommendationStatus(viewModel.voucherRecommendationStatus.value
                                        ?: VoucherRecommendationStatus.WITH_RECOMMENDATION)
                            } else {
                                validation.run {
                                    // show the discount nominal error and store the error state in the view model
                                    benefitPercentError.setPercentageTextFieldError(discountAmountTextFieldModel)
                                    if (benefitPercentError.isNotBlank()) viewModel.addErrorPair(isError, benefitPercentError, PromotionType.Cashback.Percentage.Amount)
                                    // show the max discount error and store the error state in the view model
                                    benefitMaxError.setPercentageTextFieldError(percentageMaximumDiscountTextFieldModel)
                                    if (benefitMaxError.isNotBlank()) viewModel.addErrorPair(isError, benefitMaxError, PromotionType.Cashback.Percentage.MaximumDiscount)
                                    // show the min purchase error and store the error state in the view model
                                    minPurchaseError.setPercentageTextFieldError(percentageMinimumPurchaseTextFieldModel)
                                    if (minPurchaseError.isNotBlank()) viewModel.addErrorPair(isError, minPurchaseError, PromotionType.Cashback.Percentage.MinimumPurchase)

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
                            // disable the next button when an error occurred
                            nextButtonUiModel.isEnabled = !isError
                            // store the percentage input error state to preserve the next button state
                            viewModel.isPercentageInputError = isError
                        }
                        is Fail -> {
                            // show user friendly error message to user
                            val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                            view?.showErrorToaster(errorMessage)
                            // send crash report to firebase crashlytics
                            MvcErrorHandler.logToCrashlytics(result.throwable, VALIDATE_PERCENTAGE_INPUT_ERROR)
                            // log error type to scalyr
                            ServerLogger.log(Priority.P2, "MVC_VALIDATE_PERCENTAGE_INPUT_ERROR", mapOf("type" to errorMessage))
                        }
                    }
                    adapter.notifyDataSetChanged()
                    isPercentageWaitingForValidation = false
                }
            }
            observe(viewModel.idrVoucherRecommendationResult) { result ->
                when (result) {
                    is Success -> {
                        val recommendationData = result.data
                        viewModel.updateVoucherRecommendation(CashbackType.Rupiah, recommendationData)
                        val isActiveCashBackType = activeCashbackType is CashbackType.Rupiah
                        // only applied the recommendation data for new voucher creation process
                        if (isCreateNew ) {
                            updateTextFieldValues(CashbackType.Rupiah, recommendationData)
                            // only render and calculate the recommendation data if the cash back type is active
                            if (isActiveCashBackType) {
                                adapter.notifyDataSetChanged()
                                rupiahCashbackTextFieldList.forEach { uiModel ->
                                    viewModel.addTextFieldValueToCalculation(uiModel.currentValue, uiModel.promotionType)
                                }
                            }
                        }
                        // only change the ticker content based on the active cash back type
                        if (isActiveCashBackType) viewModel.updateRecommendationStatus(CashbackType.Rupiah)
                    }
                    is Fail -> {
                        // use default values when the api call failed
                        viewModel.updateVoucherRecommendation(CashbackType.Rupiah, viewModel.getStaticRecommendationData())
                        if (activeCashbackType is CashbackType.Rupiah) viewModel.updateRecommendationStatus(CashbackType.Rupiah)
                        // show user friendly error message to user
                        val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                        view?.showErrorToaster(errorMessage)
                        // send crash report to firebase crashlytics
                        MvcErrorHandler.logToCrashlytics(result.throwable, GET_RUPIAH_VOUCHER_RECOMMENDATION_ERROR)
                        // log error type to scalyr
                        ServerLogger.log(Priority.P2, "MVC_GET_RUPIAH_VOUCHER_RECOMMENDATION_ERROR", mapOf("type" to errorMessage))
                    }
                }
            }
            observe(viewModel.percentageVoucherRecommendationResult) { result ->
                when (result) {
                    is Success -> {
                        val recommendationData = result.data
                        viewModel.updateVoucherRecommendation(CashbackType.Percentage, recommendationData)
                        val isActiveCashBackType = activeCashbackType is CashbackType.Percentage
                        // only applied the recommendation data for new voucher creation process
                        if (isCreateNew) {
                            updateTextFieldValues(CashbackType.Percentage, recommendationData)
                            // only render and calculate the recommendation data if the cash back type is active
                            if (isActiveCashBackType) {
                                adapter.notifyDataSetChanged()
                                percentageCashbackTextFieldList.forEach { uiModel ->
                                    viewModel.addTextFieldValueToCalculation(uiModel.currentValue, uiModel.promotionType)
                                }
                            }
                        }
                        // only change the ticker content based on the active cash back type
                        if (isActiveCashBackType) viewModel.updateRecommendationStatus(CashbackType.Percentage)
                    }
                    is Fail -> {
                        // use default values when the api call failed
                        viewModel.updateVoucherRecommendation(CashbackType.Percentage, viewModel.getStaticRecommendationData())
                        if (activeCashbackType is CashbackType.Percentage) viewModel.updateRecommendationStatus(CashbackType.Percentage)
                        // show user friendly error message to user
                        val errorMessage = ErrorHandler.getErrorMessage(context, result.throwable)
                        view?.showErrorToaster(errorMessage)
                        // send crash report to firebase crashlytics
                        MvcErrorHandler.logToCrashlytics(result.throwable, GET_PERCENTAGE_VOUCHER_RECOMMENDATION_ERROR)
                        // log error type to scalyr
                        ServerLogger.log(Priority.P2, "MVC_GET_PERCENTAGE_VOUCHER_RECOMMENDATION_ERROR", mapOf("type" to errorMessage))
                    }
                }
            }
            observe(viewModel.voucherRecommendationStatus) { status ->
                recommendationTickerUiModel.isRecommendationApplied = status == VoucherRecommendationStatus.WITH_RECOMMENDATION
                with(topSectionUiModelList) {
                    adapter.notifyItemChanged(indexOf(recommendationTickerUiModel))
                }
            }
        }
    }

    private fun getCashbackTypeUiList(): List<Visitable<*>> {
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
                percentageExpenseBottomSheet?.show(childFragmentManager, CashbackExpenseInfoBottomSheetFragment.TAG)
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
        when (cashbackType) {
            CashbackType.Rupiah -> {
                adapter.data.add(textFieldIndex, rupiahCashbackAdapterUiModel)
                cashbackTypeEventAction = VoucherCreationAnalyticConstant.EventAction.Click.CASHBACK_TYPE_RUPIAH
                // disable the next button base on the input state
                nextButtonUiModel.isEnabled = !viewModel.isRupiahInputError
            }
            CashbackType.Percentage -> {
                adapter.data.add(textFieldIndex, percentageCashbackAdapterUiModel)
                cashbackTypeEventAction = VoucherCreationAnalyticConstant.EventAction.Click.CASHBACK_TYPE_PERCENTAGE
                // disable the next button base on the input state
                nextButtonUiModel.isEnabled = !viewModel.isPercentageInputError
            }
        }
        VoucherCreationTracking.sendCreateVoucherClickTracking(
                step = VoucherCreationStep.BENEFIT,
                action = cashbackTypeEventAction,
                userId = userSession.userId
        )
        viewModel.changeCashbackType(cashbackType)
        viewModel.updateRecommendationStatus(cashbackType)
        adapter.notifyItemChanged(textFieldIndex)
        // tell adapter to update the next button state
        adapter.run {
            notifyItemChanged(data.indexOf(nextButtonUiModel))
        }
    }

    private fun onTextFieldValueChanged(value: Int?, type: PromotionType) {
        (type as? PromotionType.Cashback)?.let {
            viewModel.addTextFieldValueToCalculation(value, it)
            viewModel.updateRecommendationStatus(activeCashbackType)
        }
    }

    private fun onSetErrorMessage(isError: Boolean, errorMessage: String?, type: PromotionType) {
        (type as? PromotionType.Cashback)?.let {
            viewModel.addErrorPair(isError, errorMessage.toBlankOrString(), it)
        }
        // update view model state
        when (activeCashbackType) {
            CashbackType.Rupiah -> {
                viewModel.isRupiahInputError = isError
            }
            CashbackType.Percentage -> {
                viewModel.isPercentageInputError = isError
            }
        }
        // disable next button when error occurred
        nextButtonUiModel.isEnabled = !isError
        adapter.run {
            notifyItemChanged(data.indexOf(nextButtonUiModel))
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
        expensesInfoBottomSheetFragment.show(childFragmentManager, GeneralExpensesInfoBottomSheetFragment.TAG)
    }

    override fun onClickableSpanClicked() {
        val voucherRecommendation = viewModel.getVoucherRecommendationData(activeCashbackType)
        updateTextFieldValues(activeCashbackType, voucherRecommendation)
        when (activeCashbackType) {
            CashbackType.Rupiah -> {
                rupiahCashbackTextFieldList.forEach { uiModel ->
                    viewModel.addTextFieldValueToCalculation(uiModel.currentValue, uiModel.promotionType)
                    viewModel.isRupiahInputError = false
                }
            }
            CashbackType.Percentage -> {
                percentageCashbackTextFieldList.forEach { uiModel ->
                    viewModel.addTextFieldValueToCalculation(uiModel.currentValue, uiModel.promotionType)
                    viewModel.isPercentageInputError = false
                }
            }
        }

        // reset the error states stored in view model
        viewModel.resetErrorPairList(activeCashbackType)

        adapter.notifyDataSetChanged()

        // update the recommendation status
        viewModel.updateRecommendationStatus(activeCashbackType)

        // enable the next button after the recommendation applied
        adapter.run {
            nextButtonUiModel.isEnabled = true
            notifyItemChanged(data.indexOf(nextButtonUiModel))
        }
    }

    private fun updateTextFieldValues(cashbackType: CashbackType, voucherRecommendationData: VoucherRecommendationData) {
        when (cashbackType) {
            CashbackType.Rupiah -> {
                setRupiahTextFieldValue(rupiahMaximumDiscountTextFieldModel, voucherRecommendationData.voucherDiscountAmtMax)
                setRupiahTextFieldValue(rupiahMinimumPurchaseTextFieldModel, voucherRecommendationData.voucherMinimumAmt)
                setRupiahTextFieldValue(rupiahVoucherQuotaTextFieldModel, voucherRecommendationData.voucherQuota)
            }
            CashbackType.Percentage -> {
                setPercentageTextFieldValue(discountAmountTextFieldModel, voucherRecommendationData.voucherDiscountAmt)
                setPercentageTextFieldValue(percentageMaximumDiscountTextFieldModel, voucherRecommendationData.voucherDiscountAmtMax)
                setPercentageTextFieldValue(percentageMinimumPurchaseTextFieldModel, voucherRecommendationData.voucherMinimumAmt)
                setPercentageTextFieldValue(percentageVoucherQuotaTextFieldModel, voucherRecommendationData.voucherQuota)
            }
        }
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

    private fun setRupiahTextFieldValue(voucherTextFieldUiModel: VoucherTextFieldUiModel, newValue: Int) {
        rupiahCashbackTextFieldList.run {
            get(indexOf(voucherTextFieldUiModel)).currentValue = newValue
        }
    }

    private fun String.setRupiahTextFieldError(voucherTextFieldUiModel: VoucherTextFieldUiModel) {
        if (isNotBlank()) {
            rupiahCashbackTextFieldList.run {
                get(indexOf(voucherTextFieldUiModel)).currentErrorPair = Pair(true, this@setRupiahTextFieldError)
            }
        }
    }

    private fun setPercentageTextFieldValue(voucherTextFieldUiModel: VoucherTextFieldUiModel, newValue: Int) {
        percentageCashbackTextFieldList.run {
            get(indexOf(voucherTextFieldUiModel)).currentValue = newValue
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

