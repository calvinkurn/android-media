package com.tokopedia.vouchercreation.create.data.source

import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldType
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.create.view.uimodel.vouchertype.item.PromotionTypeTickerUiModel

object PromotionTypeUiListStaticDataSource {

    fun getFreeDeliveryTypeUiList()= listOf(
            PromotionTypeTickerUiModel(R.string.mvc_create_promo_type_free_deliv_ticker),
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.CURRENCY,
                    labelRes = R.string.mvc_create_promo_type_free_deliv_textfield_free_deliv_amount,
                    minValue = MinValue.NOMINAL_AMOUNT,
                    maxValue = MaxValue.NOMINAL_AMOUNT,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum),
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.CURRENCY,
                    labelRes = R.string.mvc_create_promo_type_textfield_minimum_purchase,
                    minValue = MinValue.PUCHASE_AMOUNT,
                    maxValue = MaxValue.PUCHASE_AMOUNT,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum),
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.QUANTITY,
                    labelRes = R.string.mvc_create_promo_type_textfield_voucher_quota,
                    minValue = MinValue.VOUCHER_QUOTA,
                    maxValue = MaxValue.VOUCHER_QUOTA,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                    isLastTextField = true)
    )

    object MinValue {
        const val NOMINAL_AMOUNT = 5000
        const val PUCHASE_AMOUNT = 50000
        const val VOUCHER_QUOTA = 1
        const val DISCOUNT_AMOUNT = 5
        const val MAXIMUM_DISCOUNT = 50000
    }

    object MaxValue {
        const val NOMINAL_AMOUNT = 99999999
        const val PUCHASE_AMOUNT = 99999999
        const val VOUCHER_QUOTA = 999
        const val DISCOUNT_AMOUNT = 100
        const val MAXIMUM_DISCOUNT = 99999999
    }

}