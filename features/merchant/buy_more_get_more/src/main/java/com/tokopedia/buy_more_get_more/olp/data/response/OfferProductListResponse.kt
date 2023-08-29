package com.tokopedia.buy_more_get_more.olp.data.response

import com.google.gson.annotations.SerializedName

data class OfferProductListResponse(
    @SerializedName("GetOfferingProductList")
    val offeringProductList: OfferProductList = OfferProductList()
) {

    data class OfferProductList(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("products")
        val productList: List<Product> = emptyList(),
        @SerializedName("total_product")
        val totalProduct: Int = 0
    )

    data class ResponseHeader(
        @SerializedName("success")
        val success: Boolean = true,
        @SerializedName("errorMessage")
        val errorMessage: List<String> = emptyList(),
        @SerializedName("errorCode")
        val errorCode: Long = 0
    )

    data class Product(
        @SerializedName("offer_id")
        val offerId: Long = 0,
        @SerializedName("parent_id")
        val parentId: Long = 0,
        @SerializedName("product_id")
        val productId: Long = 0,
        @SerializedName("warehouse_id")
        val warehouseId: Long = 0,
        @SerializedName("product_url")
        val productUrl: String = "",
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
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("is_vbs")
        val isVbs: Boolean = false,
        @SerializedName("campaign")
        val campaign: Campaign = Campaign(),
        @SerializedName("label_group")
        val labelGroup: List<LabelGroup> = emptyList()
    ) {
        data class Campaign(
            @SerializedName("name")
            val name: String = "",
            @SerializedName("original_price")
            val originalPrice: String = "",
            @SerializedName("discounted_price")
            val discountedPrice: String = "",
            @SerializedName("discounted_percentage")
            val discountedPercentage: Int = 0,
            @SerializedName("custom_stock")
            val customStock: Int = 0
        )

        data class LabelGroup(
            @SerializedName("position")
            val position: String = "",
            @SerializedName("title")
            val title: String = "",
            @SerializedName("type")
            val type: String = "",
            @SerializedName("url")
            val url: String = ""
        )
    }
}
