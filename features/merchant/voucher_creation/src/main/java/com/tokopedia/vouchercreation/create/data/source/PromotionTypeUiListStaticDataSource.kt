package com.tokopedia.vouchercreation.create.data.source

import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldType
import com.tokopedia.vouchercreation.common.view.textfield.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.create.view.enums.PromotionType

object PromotionTypeUiListStaticDataSource {

    fun getCashbackRupiahTypeTextFieldList(onValueChanged: (Int?, PromotionType) -> Unit) = listOf(
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.CURRENCY,
                    labelRes = R.string.mvc_create_promo_type_cashback_textfield_discount_maximum,
                    minValue = MinValue.NOMINAL_AMOUNT,
                    maxValue = MaxValue.NOMINAL_AMOUNT,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                    promotionType = PromotionType.Cashback.Rupiah.MaximumDiscount,
                    onValueChanged = onValueChanged),
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.CURRENCY,
                    labelRes = R.string.mvc_create_promo_type_textfield_minimum_purchase,
                    minValue = MinValue.PUCHASE_AMOUNT,
                    maxValue = MaxValue.PUCHASE_AMOUNT,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                    promotionType = PromotionType.Cashback.Rupiah.MinimumPurchase,
                    onValueChanged = onValueChanged),
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.QUANTITY,
                    labelRes = R.string.mvc_create_promo_type_textfield_voucher_quota,
                    minValue = MinValue.VOUCHER_QUOTA,
                    maxValue = MaxValue.VOUCHER_QUOTA,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                    promotionType = PromotionType.Cashback.Rupiah.VoucherQuota,
                    onValueChanged = onValueChanged)

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