package com.tokopedia.cartrevamp.view.uimodel

data class CartDetailInfo(
    var cartDetailType: String = "",
    var bmGmData: BmGmData = BmGmData(),
    var bmGmTiersAppliedList: ArrayList<BmGmTiersAppliedData> = arrayListOf()
) {
    data class BmGmData(
        var offerId: Long = 0L,
        var offerJsonData: String = ""
    )
    data class BmGmTiersAppliedData(
        var tierId: Long = 0L,
        var listProduct: ArrayList<BmGmProductData> = arrayListOf()
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
