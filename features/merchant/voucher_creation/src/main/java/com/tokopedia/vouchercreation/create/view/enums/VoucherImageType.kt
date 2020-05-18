package com.tokopedia.vouchercreation.create.view.enums

import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import com.tokopedia.vouchercreation.R

sealed class VoucherImageType(val value: Int) {
    class FreeDelivery(value: Int) : VoucherImageType(value)
    class Rupiah(value: Int) : VoucherImageType(value)
    class Percentage(value: Int, val percentage: Int) : VoucherImageType(value)
}

enum class VoucherImageTextType(@DimenRes val dimenRes: Int) {
    VALUE(R.dimen.mvc_voucher_preview_value_text_size),
    SCALE(R.dimen.mvc_voucher_preview_scale_text_size),
    ASTERIX(R.dimen.mvc_voucher_preview_asterix_text_size)
}

enum class ValueScaleType(@StringRes val stringRes: Int) {
    THOUSAND(R.string.mvc_rb),
    MILLION(R.string.mvc_jt)
}

object CurrencyScale {
    internal const val THOUSAND = 1000
    internal const val MILLION = 1000000
    internal const val BILLION = 1000000000
}
