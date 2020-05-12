package com.tokopedia.vouchercreation.create.view.enums

import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import com.tokopedia.vouchercreation.R

sealed class VoucherImageType {
    data class FreeDelivery(val value: Int) : VoucherImageType()
    data class Rupiah(val value: Int) : VoucherImageType()
    data class Percentage(val value: Int, val percentage: Int) : VoucherImageType()
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
