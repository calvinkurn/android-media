package com.tokopedia.vouchercreation.create.view.enums

sealed class PromotionTextField {
    sealed class FreeDelivery : PromotionTextField() {
        object Amount : FreeDelivery()
        object MinimumPurchase : FreeDelivery()
        object VoucherQuota : FreeDelivery()
    }
    sealed class Cashback : PromotionTextField() {
        sealed class Rupiah : Cashback() {
            object MaximumDiscount : Rupiah()
            object MinimumPurchase : Rupiah()
            object VoucherQuota : Rupiah()
        }
        sealed class Percentage : Cashback() {
            object Amount : Cashback()
            object MaximumDiscount : Cashback()
            object MinimumPurchase : Cashback()
            object VoucherQuota : Cashback()
        }
    }
}