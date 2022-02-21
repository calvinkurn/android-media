package com.tokopedia.vouchercreation.create.view.enums

import androidx.annotation.StringDef
import androidx.annotation.StringRes
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherTypeConst

sealed class VoucherImageType(val value: Int,
                              @BenefitType val benefitType: String,
                              @CouponType val couponType: String) {
    class FreeDelivery(value: Int) : VoucherImageType(value, BenefitType.IDR, CouponType.SHIPPING)
    class Rupiah(value: Int) : VoucherImageType(value, BenefitType.IDR, CouponType.CASHBACK)
    class Percentage(value: Int, val percentage: Int) : VoucherImageType(value, BenefitType.PERCENT, CouponType.CASHBACK)
}

enum class VoucherImageTextType(val textSize: Int) {
    VALUE(R.dimen.mvc_preview_value_text_size),
    SCALE(R.dimen.mvc_preview_scale_text_size),
    ASTERISK(R.dimen.mvc_preview_asterix_text_size)
}

enum class PostImageTextType(val textSize: Int) {
    VALUE(R.dimen.mvc_square_voucher_preview_value_text_size),
    SCALE(R.dimen.mvc_square_voucher_preview_scale_text_size),
    ASTERISK(R.dimen.mvc_square_voucher_asterix_text_size)
}

enum class ValueScaleType(@StringRes val stringRes: Int) {
    THOUSAND(R.string.mvc_rb),
    MILLION(R.string.mvc_jt)
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(BenefitType.IDR, BenefitType.PERCENT)
annotation class BenefitType {
    companion object {
        const val IDR = "idr"
        const val PERCENT = "percent"
    }
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(CouponType.SHIPPING, CouponType.CASHBACK, CouponType.DISCOUNT)
annotation class CouponType {
    companion object {
        const val SHIPPING = "shipping"
        const val DISCOUNT = "discount"
        const val CASHBACK = "cashback"
    }
}

fun getVoucherImageType(@VoucherTypeConst voucherType: Int,
                        @BenefitType benefitType: String,
                        amount: Int,
                        amountMax: Int): VoucherImageType? {
    when (voucherType) {
        VoucherTypeConst.FREE_ONGKIR -> {
            return VoucherImageType.FreeDelivery(amount)
        }
        VoucherTypeConst.CASHBACK -> {
            return when(benefitType) {
                BenefitType.IDR -> {
                    VoucherImageType.Rupiah(amount)
                }
                BenefitType.PERCENT -> {
                    VoucherImageType.Percentage(amountMax, amount)
                }
                else -> null
            }
        }
        else -> return null
    }
}