package com.tokopedia.shop_widget.buy_more_save_more.entity

data class OfferingProductListUiModel(
    val responseHeader: ResponseHeader = ResponseHeader(),
    val productList: List<Product> = emptyList(),
    val totalProduct: Int = 0
) {
    data class ResponseHeader(
        val success: Boolean = true,
        val errorMessage: List<String> = emptyList()
    )

    data class Product(
        val offerId: Long = 0,
        val parentId: Long = 0,
        val productId: Long = 0,
        val warehouseId: Long = 0,
        val productUrl: String = "",
        val imageUrl: String = "",
        val name: String = "",
        val price: String = "",
        val rating: String = "",
        val soldCount: Int = 0,
        val stock: Int = 0,
        val isVbs: Boolean = false,
        val minOrder: Int = 1,
        val campaign: Campaign = Campaign(),
        val labelGroup: List<LabelGroup> = emptyList(),
        val position: Int = 0,
        val totalProduct: Int = 0
    ) {
        data class Campaign(
            val name: String = "",
            val originalPrice: String = "",
            val discountedPrice: String = "",
            val discountedPercentage: Int = 0,
            val customStock: Int = 0
        )

        data class LabelGroup(
            val position: String = "",
            val title: String = "",
            val type: String = "",
            val url: String = ""
        )
    }
}
