package com.tokopedia.vouchercreation.shop.create.view.enums

import androidx.annotation.StringRes
import com.tokopedia.vouchercreation.R

sealed class PromotionType {
    sealed class FreeDelivery : PromotionType() {
        object Amount : FreeDelivery()
        object MinimumPurchase : FreeDelivery()
        object VoucherQuota : FreeDelivery()
    }

    sealed class Cashback(val cashbackType: CashbackType): PromotionType() {

        sealed class Rupiah : Cashback(CashbackType.Rupiah) {
            object MaximumDiscount : Rupiah()
            object MinimumPurchase : Rupiah()
            object VoucherQuota : Rupiah()
        }

        sealed class Percentage : Cashback(CashbackType.Percentage) {
            object Amount : Percentage()
            object MaximumDiscount : Percentage()
            object MinimumPurchase : Percentage()
            object VoucherQuota : Percentage()
        }
    }
}

sealed class CashbackType(@StringRes val chipTitleRes: Int) {
    object Rupiah: CashbackType(R.string.mvc_create_promo_type_cashback_chip_rupiah)
    object Percentage: CashbackType(R.string.mvc_create_promo_type_cashback_chip_percentage)
}