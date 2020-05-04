package com.tokopedia.vouchercreation.create.view.fragment.vouchertype

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.promotionexpense.PromotionExpenseEstimationUiModel
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldType
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.create.data.source.PromotionTypeUiListStaticDataSource
import com.tokopedia.vouchercreation.create.view.enums.CashbackType
import com.tokopedia.vouchercreation.create.view.enums.PromotionTextField
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeItemAdapterFactory
import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.CashbackTypePickerUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeInputListUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeTickerUiModel

class CashbackVoucherCreateFragment(onNextStep: () -> Unit) : BaseListFragment<Visitable<*>, PromotionTypeItemAdapterFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(onNextStep: () -> Unit = {}) = CashbackVoucherCreateFragment(onNextStep)

        private const val INPUT_FIELD_ADAPTER_SIZE = 1
    }

    private val promoDescTickerModel by lazy {
        PromotionTypeTickerUiModel(R.string.mvc_create_promo_type_cashback_ticker, ::onDismissTicker)
    }

    private val cashbackTypePickerModel by lazy {
        CashbackTypePickerUiModel(::onCashbackSelectedType)
    }

    private val maximumDiscountTextFieldModel =
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.CURRENCY,
                labelRes = R.string.mvc_create_promo_type_cashback_textfield_discount_maximum,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.NOMINAL_AMOUNT,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.NOMINAL_AMOUNT,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                onValueChanged = ::onTextFieldValueChanged)


    private val minimumPurchaseTextFieldModel =
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.CURRENCY,
                labelRes = R.string.mvc_create_promo_type_textfield_minimum_purchase,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.PUCHASE_AMOUNT,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.PUCHASE_AMOUNT,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                onValueChanged = ::onTextFieldValueChanged)


    private val voucherQuotaTextFieldModel =
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.QUANTITY,
                labelRes = R.string.mvc_create_promo_type_textfield_voucher_quota,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.VOUCHER_QUOTA,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.VOUCHER_QUOTA,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                isLastTextField = true)


    private val discountAmountTextFieldModel =
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.PERCENTAGE,
                labelRes = R.string.mvc_create_promo_type_cashback_textfield_discount_amount,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.DISCOUNT_AMOUNT,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.DISCOUNT_AMOUNT,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                isLastTextField = true)

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
        listOf(
                maximumDiscountTextFieldModel,
                minimumPurchaseTextFieldModel,
                voucherQuotaTextFieldModel
        )
    }

    private val rupiahCashbackAdapterUiModel by lazy {
        PromotionTypeInputListUiModel(rupiahCashbackTextFieldList)
    }

    private val percentageCashbackTextFieldList by lazy {
        listOf(
                discountAmountTextFieldModel,
                maximumDiscountTextFieldModel,
                minimumPurchaseTextFieldModel,
                voucherQuotaTextFieldModel
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

    override fun initInjector() {}

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
        renderList(getCashbackTypeUiList())
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
        val extraSize = INPUT_FIELD_ADAPTER_SIZE + bottomSectionUiModelList.size
        textFieldIndex = adapter.data.size - extraSize
        adapter.data.removeAt(textFieldIndex)
        when(cashbackType) {
            CashbackType.RUPIAH -> {
                adapter.data.add(textFieldIndex, rupiahCashbackAdapterUiModel)
            }
            CashbackType.PERCENTAGE -> {
                adapter.data.add(textFieldIndex, percentageCashbackAdapterUiModel)
            }
        }
        adapter.notifyDataSetChanged()
    }

    private fun onTextFieldValueChanged(value: Int?, textFieldType: PromotionTextField) {

    }
}