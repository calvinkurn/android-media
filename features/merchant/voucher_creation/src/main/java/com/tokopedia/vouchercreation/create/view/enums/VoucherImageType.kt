package com.tokopedia.vouchercreation.create.view.enums

import androidx.annotation.DimenRes
import androidx.annotation.StringDef
import androidx.annotation.StringRes
import com.tokopedia.vouchercreation.R

sealed class VoucherImageType(val value: Int,
                              @BenefitType val benefitType: String,
                              @CouponType val couponType: String) {
    class FreeDelivery(value: Int) : VoucherImageType(value, BenefitType.IDR, CouponType.SHIPPING)
    class Rupiah(value: Int) : VoucherImageType(value, BenefitType.IDR, CouponType.CASHBACK)
    class Percentage(value: Int, val percentage: Int) : VoucherImageType(value, BenefitType.PERCENT, CouponType.CASHBACK)
}

enum class VoucherImageTextType(@DimenRes val dimenRes: Int) {
    VALUE(R.dimen.mvc_voucher_preview_value_text_size),
    SCALE(R.dimen.mvc_voucher_preview_scale_text_size),
    ASTERISK(R.dimen.mvc_voucher_preview_asterix_text_size)
}

enum class PostImageTextType(@DimenRes val dimenRes: Int) {
    VALUE(R.dimen.mvc_post_preview_value_text_size),
    SCALE(R.dimen.mvc_post_preview_scale_text_size),
    ASTERISK(R.dimen.mvc_post_preview_asterix_text_size)
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
@StringDef(CouponType.SHIPPING, CouponType.CASHBACK)
annotation class CouponType {
    companion object {
        const val SHIPPING = "shipping"
        const val CASHBACK = "cashback"
    }
}