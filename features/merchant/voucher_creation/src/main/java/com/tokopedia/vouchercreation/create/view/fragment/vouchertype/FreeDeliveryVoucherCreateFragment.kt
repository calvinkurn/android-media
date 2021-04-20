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
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.toZeroIfNull
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.errorhandler.MvcErrorHandler
import com.tokopedia.vouchercreation.common.utils.dismissBottomSheetWithTags
import com.tokopedia.vouchercreation.common.utils.showErrorToaster
import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationUiModel
import com.tokopedia.vouchercreation.common.view.textfield.vouchertype.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.create.data.source.PromotionTypeUiListStaticDataSource
import com.tokopedia.vouchercreation.create.view.enums.PromotionType
import com.tokopedia.vouchercreation.create.view.enums.VoucherImageType
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.GeneralExpensesInfoBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeItemAdapterFactory
import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.voucherreview.VoucherReviewUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeInputListUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeTickerUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.FreeDeliveryVoucherCreateViewModel
import javax.inject.Inject

class FreeDeliveryVoucherCreateFragment: BaseListFragment<Visitable<*>, PromotionTypeItemAdapterFactory>() {

    companion object {@JvmStatic
        fun createInstance(onNextStep: (VoucherImageType, Int, Int) -> Unit,
                           onShouldChangeBannerValue: (VoucherImageType) -> Unit = {},
                           context: Context,
                           getVoucherReviewUiModel: () -> VoucherReviewUiModel?) = FreeDeliveryVoucherCreateFragment().apply {
            this.onNextStep = onNextStep
            this.onShouldChangeBannerValue = onShouldChangeBannerValue
            viewContext = context
            this.getVoucherReviewUiModel = getVoucherReviewUiModel
        }

        private const val TICKER_INDEX_POSITION = 0

        private const val ERROR_MESSAGE = "Error validate free delivery voucher"
    }
    private var onNextStep: (VoucherImageType, Int, Int) -> Unit = { _,_,_ -> }
    private var onShouldChangeBannerValue: (VoucherImageType) -> Unit = {}
    private var viewContext = context
    private var getVoucherReviewUiModel: () -> VoucherReviewUiModel? = { null }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(FreeDeliveryVoucherCreateViewModel::class.java)
    }

    private val promoDescTickerModel by lazy {
        PromotionTypeTickerUiModel(R.string.mvc_create_promo_type_free_deliv_ticker, ::onDismissTicker)
    }

    private val freeDeliveryExpenseInfoBottomSheetField by lazy {
        GeneralExpensesInfoBottomSheetFragment.createInstance(context).apply {
            setTitle(viewContext?.getString(R.string.mvc_create_promo_type_bottomsheet_title_promo_expenses).toBlankOrString())
        }
    }

    private val freeDeliveryAmountTextFieldModel by lazy {
        PromotionTypeUiListStaticDataSource.getFreeDeliveryAmountTextFieldUiModel(::onTextFieldValueChanged, ::onSetErrorMessage).apply {
            getVoucherReviewUiModel()?.let { data ->
                if (data.voucherType is VoucherImageType.FreeDelivery) {
                    currentValue = data.voucherType.value
                }
            }
        }
    }
    private val minimumPurchaseTextFieldModel by lazy {
        PromotionTypeUiListStaticDataSource.getMinimumPurchaseTextFieldUiModel(::onTextFieldValueChanged, ::onSetErrorMessage, PromotionType.FreeDelivery.MinimumPurchase).apply {
            getVoucherReviewUiModel()?.let { data ->
                if (data.voucherType is VoucherImageType.FreeDelivery) {
                    currentValue = data.minPurchase
                }
            }
        }
    }
    private val voucherQuotaTextFieldModel by lazy {
        PromotionTypeUiListStaticDataSource.getVoucherQuotaTextFieldUiModel(::onTextFieldValueChanged, ::onSetErrorMessage, PromotionType.FreeDelivery.VoucherQuota).apply {
            getVoucherReviewUiModel()?.let { data ->
                if (data.voucherType is VoucherImageType.FreeDelivery) {
                    currentValue = data.voucherQuota
                }
            }
        }
    }

    private val freeDeliveryTextFieldsList by lazy {
        listOf(
                freeDeliveryAmountTextFieldModel,
                minimumPurchaseTextFieldModel,
                voucherQuotaTextFieldModel
        )
    }

    private val freeDeliveryTextFieldsUiModel by lazy {
        freeDeliveryTextFieldsList.run {
            if (::viewModelFactory.isInitialized) {
                forEach { uiModel ->
                    viewModel.addTextFieldValueToCalculation(uiModel.currentValue, uiModel.promotionType)
                }
            }
            PromotionTypeInputListUiModel(this)
        }
    }

    private val nextButtonUiModel by lazy {
        NextButtonUiModel(::validateValues, true)
    }

    private val promotionExpenseEstimationUiModel =
            PromotionExpenseEstimationUiModel(
                    onTooltipClicked = ::onTooltipClicked
            )

    private val freeDeliveryTypeUiList by lazy {
        mutableListOf(
                promoDescTickerModel,
                freeDeliveryTextFieldsUiModel,
                promotionExpenseEstimationUiModel,
                nextButtonUiModel
        )
    }

    private var isWaitingForValidation = false

    private var voucherImageType : VoucherImageType = VoucherImageType.FreeDelivery(0)

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        observeLiveData()
        return inflater.inflate(R.layout.fragment_voucher_promotion_type, container, false)
    }

    override fun getRecyclerViewResourceId(): Int = R.id.rvMvcVoucherType

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        getVoucherReviewUiModel()?.run {
            viewModel.refreshTextFieldValue(true)
        }

        // We actually don't need to notify adapter data as the data has not been changed
        // But, as we used view pager and each fragment has different height (which will cut some layout when page changes according to previous page),
        // we will notify the adapter to mimic layout refresh
        adapter.run {
            notifyItemChanged(dataSize - 1)
        }
    }

    override fun onPause() {
        super.onPause()
        childFragmentManager.dismissBottomSheetWithTags(GeneralExpensesInfoBottomSheetFragment.TAG)
    }

    private fun setupView() {
        renderList(freeDeliveryTypeUiList)
    }

    private fun observeLiveData() {
        viewLifecycleOwner.run {
            observe(viewModel.expensesExtimationLiveData) { value ->
                promotionExpenseEstimationUiModel.estimationValue = value
                adapter.run {
                    notifyItemChanged(data.indexOf(promotionExpenseEstimationUiModel))
                }
            }
            observe(viewModel.valueListLiveData) { valueList ->
                freeDeliveryTextFieldsList.forEachIndexed { index, voucherTextFieldUiModel ->
                    voucherTextFieldUiModel.currentValue = valueList[index]
                }
            }
            observe(viewModel.errorPairListLiveData) { errorPairList ->
                freeDeliveryTextFieldsList.forEachIndexed { index, voucherTextFieldUiModel ->
                    voucherTextFieldUiModel.currentErrorPair = errorPairList[index]
                }
            }
            observe(viewModel.voucherImageValueLiveData) { imageValue ->
                voucherImageType = imageValue
                onShouldChangeBannerValue(imageValue)
            }
            observe(viewModel.freeDeliveryValidationLiveData) { result ->
                if (isWaitingForValidation) {
                    viewModel.refreshTextFieldValue()
                    when(result) {
                        is Success -> {
                            val validation = result.data
                            if (!validation.getIsHaveError()) {
                                onNextStep(voucherImageType, getMinimumPurchaseValue(), getVoucherQuotaValue())
                            } else {
                                validation.run {
                                    benefitIdrError.setTextFieldError(freeDeliveryAmountTextFieldModel)
                                    minPurchaseError.setTextFieldError(minimumPurchaseTextFieldModel)
                                    quotaError.setTextFieldError(voucherQuotaTextFieldModel)
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
                    isWaitingForValidation = false
                }
            }
        }
    }

    private fun onDismissTicker() {
        adapter.data.remove(promoDescTickerModel)
        adapter.notifyItemRemoved(TICKER_INDEX_POSITION)
    }

    private fun onTextFieldValueChanged(value: Int?, type: PromotionType) {
        if (type is PromotionType.FreeDelivery) {
            viewModel.addTextFieldValueToCalculation(value, type)
        }
    }

    private fun onSetErrorMessage(isError: Boolean, errorMessage: String?, type: PromotionType) {
        (type as? PromotionType.FreeDelivery)?.let {
            viewModel.addErrorPair(isError, errorMessage.toBlankOrString(), it)
        }
    }

    private fun onTooltipClicked() {
        freeDeliveryExpenseInfoBottomSheetField.show(childFragmentManager, GeneralExpensesInfoBottomSheetFragment.TAG)
    }

    private fun validateValues() {
        isWaitingForValidation = true
        viewModel.validateFreeDeliveryValues()
    }

    private fun String.setTextFieldError(voucherTextFieldUiModel: VoucherTextFieldUiModel) {
        if (isNotBlank()) {
            freeDeliveryTextFieldsList.run {
                get(indexOf(voucherTextFieldUiModel)).currentErrorPair = Pair(true, this@setTextFieldError)
            }
        }
    }

    private fun getMinimumPurchaseValue() =
            with(freeDeliveryTextFieldsList) {
                get(indexOf(minimumPurchaseTextFieldModel)).currentValue.toZeroIfNull()
            }

    private fun getVoucherQuotaValue() =
            with(freeDeliveryTextFieldsList) {
                get(indexOf(voucherQuotaTextFieldModel)).currentValue.toZeroIfNull()
            }
}