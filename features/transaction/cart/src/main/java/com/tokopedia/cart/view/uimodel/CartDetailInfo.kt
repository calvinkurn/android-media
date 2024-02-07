package com.tokopedia.cart.view.uimodel

data class CartDetailInfo(
    var cartDetailType: String = "",
    var bmGmData: BmGmData = BmGmData(),
    var bmGmTierProductList: ArrayList<BmGmTierProductData> = arrayListOf()
) {
    companion object {
        private const val GWP_OFFER_TYPE_ID = 2L
    }

    fun isGiftWithPurchase(): Boolean {
        return bmGmData.offerTypeId == GWP_OFFER_TYPE_ID
    }

    fun isValidGiftPurchase(): Boolean {
        return isGiftWithPurchase() && (bmGmTierProductList.firstOrNull()?.purchaseBenefitData?.purchaseBenefitProducts?.isNotEmpty() == true)
    }

    data class BmGmData(
        var offerId: Long = 0L,
        var offerTypeId: Long = 0L,
        var offerName: String = "",
        var offerIcon: String = "",
        var offerMessage: List<String> = emptyList(),
        var offerLandingPageLink: String = "",
        var totalDiscount: Double = 0.0,
        var offerJsonData: String = ""
    )

    data class BmGmTierProductData(
        var tierId: Long = 0L,
        var listProduct: ArrayList<BmGmProductData> = arrayListOf(),
        var purchaseBenefitData: CartPurchaseBenefitData = CartPurchaseBenefitData()
    ) {
        data class BmGmProductData(
            var cartId: String = "",
            var shopId: String = "",
            var productId: String = "",
            var warehouseId: Long = 0L,
            var qty: Int = 0,
            var finalPrice: Double = 0.0,
            var checkboxState: Boolean = false
        )
    }
}
