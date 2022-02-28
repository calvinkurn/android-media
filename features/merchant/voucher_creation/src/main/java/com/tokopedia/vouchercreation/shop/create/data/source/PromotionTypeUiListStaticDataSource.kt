package com.tokopedia.vouchercreation.shop.create.data.source

import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.view.textfield.vouchertype.VoucherTextFieldType
import com.tokopedia.vouchercreation.common.view.textfield.vouchertype.VoucherTextFieldUiModel
import com.tokopedia.vouchercreation.shop.create.view.enums.PromotionType

object PromotionTypeUiListStaticDataSource {

    fun<T : PromotionType> getMinimumPurchaseTextFieldUiModel(onValueChanged: (Int?, PromotionType) -> Unit,
                                           onSetErrorMessage: (Boolean, String?, PromotionType) -> Unit,
                                           promotionType: T) =
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.CURRENCY,
                    labelRes = R.string.mvc_create_promo_type_textfield_minimum_purchase,
                    minValue = MinValue.PUCHASE_AMOUNT,
                    maxValue = MaxValue.PUCHASE_AMOUNT,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                    currentValue = InitialValue.MINIMUM_PURCHASE,
                    promotionType = promotionType,
                    onValueChanged = onValueChanged,
                    onSetErrorMessage = onSetErrorMessage)

    fun<T : PromotionType> getVoucherQuotaTextFieldUiModel(onValueChanged: (Int?, PromotionType) -> Unit,
                                        onSetErrorMessage: (Boolean, String?, PromotionType) -> Unit,
                                        promotionType: T) =
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.QUANTITY,
                    labelRes = R.string.mvc_create_promo_type_textfield_voucher_quota,
                    minValue = MinValue.VOUCHER_QUOTA,
                    maxValue = MaxValue.VOUCHER_QUOTA,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                    currentValue = InitialValue.VOUCHER_QUOTA,
                    promotionType = promotionType,
                    onValueChanged = onValueChanged,
                    onSetErrorMessage = onSetErrorMessage)

    fun getFreeDeliveryAmountTextFieldUiModel(onValueChanged: (Int?, PromotionType) -> Unit,
                                              onSetErrorMessage: (Boolean, String?, PromotionType) -> Unit) =
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.CURRENCY,
                    labelRes = R.string.mvc_create_promo_type_free_deliv_textfield_free_deliv_amount,
                    minValue = MinValue.NOMINAL_AMOUNT,
                    maxValue = MaxValue.NOMINAL_AMOUNT,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                    currentValue = InitialValue.AMOUNT,
                    promotionType = PromotionType.FreeDelivery.Amount,
                    onValueChanged = onValueChanged,
                    onSetErrorMessage = onSetErrorMessage)

    fun<T: PromotionType.Cashback> getCashbackMaximumDiscountTextFieldUiModel(onValueChanged: (Int?, PromotionType) -> Unit,
                                                                     onSetErrorMessage: (Boolean, String?, PromotionType) -> Unit,
                                                                     promotionType: T) =
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.CURRENCY,
                    labelRes = R.string.mvc_create_promo_type_cashback_textfield_discount_maximum,
                    minValue = MinValue.NOMINAL_AMOUNT,
                    maxValue = MaxValue.NOMINAL_AMOUNT,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                    currentValue = InitialValue.MAXIMUM_DISCOUNT,
                    promotionType = promotionType,
                    onValueChanged = onValueChanged,
                    onSetErrorMessage = onSetErrorMessage)

    fun getCashbackPercentageDiscountAmountTextFieldUiModel(onValueChanged: (Int?, PromotionType) -> Unit,
                                                            onSetErrorMessage: (Boolean, String?, PromotionType) -> Unit) =
            VoucherTextFieldUiModel(
                    type = VoucherTextFieldType.PERCENTAGE,
                    labelRes = R.string.mvc_create_promo_type_cashback_textfield_discount_amount,
                    minValue = MinValue.DISCOUNT_AMOUNT,
                    maxValue = MaxValue.DISCOUNT_AMOUNT,
                    minAlertRes = R.string.mvc_create_promo_type_textfield_alert_minimum,
                    maxAlertRes = R.string.mvc_create_promo_type_textfield_alert_maximum,
                    currentValue = InitialValue.DISCOUNT,
                    promotionType = PromotionType.Cashback.Percentage.Amount,
                    onValueChanged = onValueChanged,
                    onSetErrorMessage = onSetErrorMessage)


    object MinValue {
        internal const val NOMINAL_AMOUNT = 5000
        internal const val PUCHASE_AMOUNT = 50000
        internal const val VOUCHER_QUOTA = 1
        internal const val DISCOUNT_AMOUNT = 0
    }

    object MaxValue {
        internal const val NOMINAL_AMOUNT = 99999999
        internal const val PUCHASE_AMOUNT = 99999999
        internal const val VOUCHER_QUOTA = 999
        internal const val DISCOUNT_AMOUNT = 100
    }
    object InitialValue {
        internal const val AMOUNT = 20000
        internal const val MINIMUM_PURCHASE = 100000
        internal const val VOUCHER_QUOTA = 100
        internal const val DISCOUNT = 10
        internal const val MAXIMUM_DISCOUNT = 20000
    }

}