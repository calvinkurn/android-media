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
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationUiModel
import com.tokopedia.vouchercreation.create.data.source.PromotionTypeUiListStaticDataSource
import com.tokopedia.vouchercreation.create.view.enums.PromotionType
import com.tokopedia.vouchercreation.create.view.fragment.bottomsheet.GeneralExpensesInfoBottomSheetFragment
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeItemAdapterFactory
import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeInputListUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeTickerUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.FreeDeliveryVoucherCreateViewModel
import javax.inject.Inject

class FreeDeliveryVoucherCreateFragment(onNextStep: () -> Unit,
                                        private val viewContext: Context): BaseListFragment<Visitable<*>, PromotionTypeItemAdapterFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(onNextStep: () -> Unit = {},
                           context: Context) = FreeDeliveryVoucherCreateFragment(onNextStep, context)

        private const val TICKER_INDEX_POSITION = 0
    }

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
            setTitle(viewContext.resources?.getString(R.string.mvc_create_promo_type_bottomsheet_title_promo_expenses).toBlankOrString())
        }
    }

    private val freeDeliveryAmountTextFieldModel by lazy {
        PromotionTypeUiListStaticDataSource.getFreeDeliveryAmountTextFieldUiModel(::onTextFieldValueChanged, ::onSetErrorMessage)
    }
    private val minimumPurchaseTextFieldModel by lazy {
        PromotionTypeUiListStaticDataSource.getMinimumPurchaseTextFieldUiModel(::onTextFieldValueChanged, ::onSetErrorMessage, PromotionType.FreeDelivery.MinimumPurchase)
    }
    private val voucherQuotaTextFieldModel by lazy {
        PromotionTypeUiListStaticDataSource.getVoucherQuotaTextFieldUiModel(::onTextFieldValueChanged, ::onSetErrorMessage, PromotionType.FreeDelivery.VoucherQuota)
    }

    private val freeDeliveryTextFieldsList =
            listOf(
                    freeDeliveryAmountTextFieldModel,
                    minimumPurchaseTextFieldModel,
                    voucherQuotaTextFieldModel
            )

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

    private val promotionExpenseEstimationUiModel =
            PromotionExpenseEstimationUiModel(
                    onTooltipClicked = ::onTooltipClicked
            )

    private val freeDeliveryTypeUiList by lazy {
        mutableListOf(
                promoDescTickerModel,
                freeDeliveryTextFieldsUiModel,
                promotionExpenseEstimationUiModel,
                NextButtonUiModel(onNextStep)
        )
    }

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
        return inflater.inflate(R.layout.fragment_free_delivery_voucher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        viewModel.refreshTextFieldValue()
        // We actually don't need to notify adapter data as the data has not been changed
        // But, as we used view pager and each fragment has different height (which will cut some layout when page changes according to previous page),
        // we will notify the adapter to mimic layout refresh
        adapter.notifyItemChanged(0)
    }

    private fun setupView() {
        observeLiveData()
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
        }
    }

    private fun onDismissTicker() {
        adapter.data.remove(promoDescTickerModel)
        adapter.notifyItemRemoved(TICKER_INDEX_POSITION)
    }

    private fun onTextFieldValueChanged(value: Int?, typeType: PromotionType) {
        if (typeType is PromotionType.FreeDelivery) {
            viewModel.addTextFieldValueToCalculation(value, typeType)
        }
    }

    private fun onSetErrorMessage(isError: Boolean, errorMessage: String?, type: PromotionType) {
        (type as? PromotionType.FreeDelivery)?.let {
            viewModel.addErrorPair(isError, errorMessage.toBlankOrString(), it)
        }
    }

    private fun onTooltipClicked() {
        freeDeliveryExpenseInfoBottomSheetField.show(childFragmentManager, GeneralExpensesInfoBottomSheetFragment::class.java.name)
    }

}