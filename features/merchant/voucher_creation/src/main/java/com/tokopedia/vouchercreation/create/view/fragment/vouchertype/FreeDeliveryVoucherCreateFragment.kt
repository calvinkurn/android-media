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
import com.tokopedia.vouchercreation.create.view.typefactory.vouchertype.PromotionTypeItemAdapterFactory
import com.tokopedia.vouchercreation.create.view.uimodel.NextButtonUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeTickerUiModel

class FreeDeliveryVoucherCreateFragment(onNextStep: () -> Unit): BaseListFragment<Visitable<*>, PromotionTypeItemAdapterFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(onNextStep: () -> Unit = {}) = FreeDeliveryVoucherCreateFragment(onNextStep)
    }

    private val promoDescTickerModel by lazy {
        PromotionTypeTickerUiModel(R.string.mvc_create_promo_type_free_deliv_ticker, ::onDismissTicker)
    }

    private val freeDeliveryAmountTextFieldModel by lazy {
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.CURRENCY,
                labelRes = R.string.mvc_create_promo_type_free_deliv_textfield_free_deliv_amount,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.NOMINAL_AMOUNT,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.NOMINAL_AMOUNT,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum)
    }

    private val minimumPurchaseTextFieldModel by lazy {
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.CURRENCY,
                labelRes = R.string.mvc_create_promo_type_textfield_minimum_purchase,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.PUCHASE_AMOUNT,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.PUCHASE_AMOUNT,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum)
    }

    private val voucherQuotaTextFieldModel by lazy {
        VoucherTextFieldUiModel(
                type = VoucherTextFieldType.QUANTITY,
                labelRes = R.string.mvc_create_promo_type_textfield_voucher_quota,
                minValue = PromotionTypeUiListStaticDataSource.MinValue.VOUCHER_QUOTA,
                maxValue = PromotionTypeUiListStaticDataSource.MaxValue.VOUCHER_QUOTA,
                minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                isLastTextField = true)
    }

    private val freeDeliveryTypeUiList = mutableListOf(
            promoDescTickerModel,
            freeDeliveryAmountTextFieldModel,
            minimumPurchaseTextFieldModel,
            voucherQuotaTextFieldModel,
            PromotionExpenseEstimationUiModel(),
            NextButtonUiModel(onNextStep)
    )

    override fun getAdapterTypeFactory(): PromotionTypeItemAdapterFactory = PromotionTypeItemAdapterFactory()

    override fun onItemClicked(t: Visitable<*>?) {}

    override fun getScreenName(): String = ""

    override fun initInjector() {

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
        adapter.notifyDataSetChanged()
    }

    private fun setupView() {
        renderList(freeDeliveryTypeUiList)
    }

    private fun onDismissTicker() {
        adapter.data.remove(promoDescTickerModel)
        adapter.notifyDataSetChanged()
    }

}