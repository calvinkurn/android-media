package com.tokopedia.vouchercreation.create.view.fragment.vouchertype

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
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationUiModel
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldType
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.create.data.source.PromotionTypeUiListStaticDataSource
import com.tokopedia.vouchercreation.create.view.enums.CashbackType
import com.tokopedia.vouchercreation.create.view.enums.PromotionType
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeItemAdapterFactory
import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.CashbackTypePickerUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeInputListUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeTickerUiModel
import com.tokopedia.vouchercreation.create.view.viewmodel.CashbackVoucherCreateViewModel
import javax.inject.Inject

class CashbackVoucherCreateFragment(onNextStep: () -> Unit) : BaseListFragment<Visitable<*>, PromotionTypeItemAdapterFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(onNextStep: () -> Unit = {}) = CashbackVoucherCreateFragment(onNextStep)

        private const val INPUT_FIELD_ADAPTER_SIZE = 1
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(CashbackVoucherCreateViewModel::class.java)
    }

    private val promoDescTickerModel by lazy {
        PromotionTypeTickerUiModel(R.string.mvc_create_promo_type_cashback_ticker, ::onDismissTicker)
    }

    private val cashbackTypePickerModel by lazy {
        CashbackTypePickerUiModel(::onCashbackSelectedType)
    }

    private val rupiahMaximumDiscountTextFieldModel =
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.CURRENCY,
                labelRes = R.string.mvc_create_promo_type_cashback_textfield_discount_maximum,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.NOMINAL_AMOUNT,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.NOMINAL_AMOUNT,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                promotionTypeType = PromotionType.Cashback.Rupiah.MaximumDiscount,
                onValueChanged = ::onTextFieldValueChanged)


    private val rupiahMinimumPurchaseTextFieldModel =
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.CURRENCY,
                labelRes = R.string.mvc_create_promo_type_textfield_minimum_purchase,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.PUCHASE_AMOUNT,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.PUCHASE_AMOUNT,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                promotionTypeType = PromotionType.Cashback.Rupiah.MinimumPurchase,
                onValueChanged = ::onTextFieldValueChanged)


    private val rupiahVoucherQuotaTextFieldModel =
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.QUANTITY,
                labelRes = R.string.mvc_create_promo_type_textfield_voucher_quota,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.VOUCHER_QUOTA,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.VOUCHER_QUOTA,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                isLastTextField = true,
                promotionTypeType = PromotionType.Cashback.Rupiah.VoucherQuota,
                onValueChanged = ::onTextFieldValueChanged)


    private val percentageMaximumDiscountTextFieldModel =
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.CURRENCY,
                    labelRes = R.string.mvc_create_promo_type_cashback_textfield_discount_maximum,
                    minValue = PromotionTypeUiListStaticDataSource.MinValue.NOMINAL_AMOUNT,
                    maxValue = PromotionTypeUiListStaticDataSource.MaxValue.NOMINAL_AMOUNT,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                    promotionTypeType = PromotionType.Cashback.Percentage.MaximumDiscount,
                    onValueChanged = ::onTextFieldValueChanged)


    private val percentageMinimumPurchaseTextFieldModel =
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.CURRENCY,
                    labelRes = R.string.mvc_create_promo_type_textfield_minimum_purchase,
                    minValue = PromotionTypeUiListStaticDataSource.MinValue.PUCHASE_AMOUNT,
                    maxValue = PromotionTypeUiListStaticDataSource.MaxValue.PUCHASE_AMOUNT,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                    promotionTypeType = PromotionType.Cashback.Percentage.MinimumPurchase,
                    onValueChanged = ::onTextFieldValueChanged)


    private val percentageVoucherQuotaTextFieldModel =
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.QUANTITY,
                    labelRes = R.string.mvc_create_promo_type_textfield_voucher_quota,
                    minValue = PromotionTypeUiListStaticDataSource.MinValue.VOUCHER_QUOTA,
                    maxValue = PromotionTypeUiListStaticDataSource.MaxValue.VOUCHER_QUOTA,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                    isLastTextField = true,
                    promotionTypeType = PromotionType.Cashback.Percentage.VoucherQuota,
                    onValueChanged = ::onTextFieldValueChanged)

    private val discountAmountTextFieldModel =
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.PERCENTAGE,
                labelRes = R.string.mvc_create_promo_type_cashback_textfield_discount_amount,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.DISCOUNT_AMOUNT,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.DISCOUNT_AMOUNT,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                isLastTextField = true,
                promotionTypeType = PromotionType.Cashback.Percentage.Amount,
                onValueChanged = ::onTextFieldValueChanged)

    private val promotionExpenseEstimationUiModel by lazy {
        PromotionExpenseEstimationUiModel()
    }

    private val nextButtonUiModel by lazy {
        NextButtonUiModel(::onNext)
    }

    private val topSectionUiModelList by lazy {
        listOf(
                promoDescTickerModel,
                cashbackTypePickerModel
        )
    }

    private val rupiahCashbackTextFieldList by lazy {
//        PromotionTypeUiListStaticDataSource.getCashbackRupiahTypeTextFieldList(::onTextFieldValueChanged)
        listOf(
                rupiahMaximumDiscountTextFieldModel,
                rupiahMinimumPurchaseTextFieldModel,
                rupiahVoucherQuotaTextFieldModel
        )
    }

    private val rupiahCashbackAdapterUiModel by lazy {
        PromotionTypeInputListUiModel(rupiahCashbackTextFieldList)
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
        PromotionTypeInputListUiModel(percentageCashbackTextFieldList)
    }

    private val bottomSectionUiModelList by lazy {
        listOf(
                promotionExpenseEstimationUiModel,
                nextButtonUiModel
        )
    }

    private var textFieldIndex = topSectionUiModelList.size - 1

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
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_free_delivery_voucher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun setupView() {
        observeLiveData()
        renderList(getCashbackTypeUiList())
    }

    private fun observeLiveData() {
        viewLifecycleOwner.observe(viewModel.expenseEstimationLiveData) { value ->
            promotionExpenseEstimationUiModel.estimationValue = value
            adapter.run {
                notifyItemChanged(data.indexOf(promotionExpenseEstimationUiModel))
            }
        }
    }

    private fun getCashbackTypeUiList() : List<Visitable<*>> {
        return mutableListOf<Visitable<*>>().apply {
            addAll(topSectionUiModelList)
            add(rupiahCashbackAdapterUiModel)
            addAll(bottomSectionUiModelList)
        }
    }

    private fun onDismissTicker() {
        adapter.data.remove(promoDescTickerModel)
        adapter.notifyDataSetChanged()
    }

    private fun onNext() {

    }

    /**
     * We need to know the index of text input fields inside the adapter as the layout size is volatile (ticker could be closed)
     * The text field index is equal to total visitables adapter size minus the bottom section model size and the input field itself
     */
    private fun onCashbackSelectedType(cashbackType: CashbackType) {
        view?.clearFocus()
        viewModel.changeCashbackType(cashbackType)

        val extraSize = INPUT_FIELD_ADAPTER_SIZE + bottomSectionUiModelList.size
        textFieldIndex = adapter.data.size - extraSize
        adapter.data.removeAt(textFieldIndex)
        when(cashbackType) {
            CashbackType.Rupiah -> {
                adapter.data.add(textFieldIndex, rupiahCashbackAdapterUiModel)
            }
            CashbackType.Percentage -> {
                adapter.data.add(textFieldIndex, percentageCashbackAdapterUiModel)
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun onTextFieldValueChanged(value: Int?, type: PromotionType) {
        (type as? PromotionType.Cashback)?.let {
            viewModel.addTextFieldValueToCalculation(value, it)
        }
    }
}