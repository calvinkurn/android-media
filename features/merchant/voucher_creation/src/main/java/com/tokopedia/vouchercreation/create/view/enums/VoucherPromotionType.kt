package com.tokopedia.vouchercreation.create.view.enums

import androidx.annotation.StringRes
import com.tokopedia.vouchercreation.R

sealed class VoucherPromotionType {
    object FreeDelivery : VoucherPromotionType()
    data class Cashback(val cashbackType: CashbackType) : VoucherPromotionType()
}

enum class CashbackType(@StringRes val chipTitleRes: Int) {
    RUPIAH(R.string.mvc_create_promo_type_cashback_chip_rupiah),
    PERCENTAGE(R.string.mvc_create_promo_type_cashback_chip_percentage)
}