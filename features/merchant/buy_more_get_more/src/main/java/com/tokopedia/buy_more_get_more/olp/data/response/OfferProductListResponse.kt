package com.tokopedia.buy_more_get_more.olp.data.response

import com.google.gson.annotations.SerializedName

data class OfferProductListResponse(
    @SerializedName("response_header")
    val responseHeader: ResponseHeader = ResponseHeader(),
    @SerializedName("products")
    val productList: List<Product> = emptyList()
) {
    data class ResponseHeader(
        @SerializedName("success")
        val success: Boolean = true,
        @SerializedName("error_code")
        val error_code: Long = 0,
        @SerializedName("process_time")
        val processTime: String = ""
    )

    data class Product(
        @SerializedName("offer_id")
        val offerId: Int = 0,
        @SerializedName("parent_id")
        val parentId: Int = 0,
        @SerializedName("product_id")
        val productId: Int = 0,
        @SerializedName("warehouse_id")
        val warehouseId: Int = 0,
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("price")
        val price: String = "",
        @SerializedName("rating")
        val rating: String = "",
        @SerializedName("sold_count")
        val soldCount: Int = 0,
        @SerializedName("min_order")
        val minOrder: Int = 0,
        @SerializedName("max_order")
        val maxOrder: Int = 0,
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("is_vbs")
        val isVbs: Boolean = false,
        @SerializedName("campaign")
        val campaign: Campaign = Campaign()
    ) {
        data class Campaign(
            @SerializedName("name")
            val name: String = "",
            @SerializedName("original_price")
            val originalPrice: String = "",
            @SerializedName("discounted_price")
            val discountedPrice: String = "",
            @SerializedName("discounted_percentage")
            val discountedPercentage: String = "",
            @SerializedName("min_order")
            val minOrder: Int = 0,
            @SerializedName("max_order")
            val maxOrder: Int = 0,
            @SerializedName("custom_stock")
            val customStock: Int = 0
        )
    }
}
