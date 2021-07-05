package com.tokopedia.shop.product.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.FreeOngkir
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup

data class ShopFeaturedProduct(
        @SerializedName("cashback")
        @Expose
        val cashback: Boolean = false,

        @SerializedName("cashback_detail")
        @Expose
        val cashbackDetail: CashbackDetail = CashbackDetail(),

        @SerializedName("image_uri")
        @Expose
        val imageUri: String = "",

        @SerializedName("is_rated")
        @Expose
        val isRated: Boolean = false,

        @SerializedName("isWishlist")
        @Expose
        val isWishlist: Boolean = false,

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("original_price")
        @Expose
        val originalPrice: String = "",

        @SerializedName("parent_id")
        @Expose
        val parentId: String = "",

        @SerializedName("percentage_amount")
        @Expose
        val percentageAmount: Int = 0,

        @SerializedName("preorder")
        @Expose
        val preorder: Boolean = false,

        @SerializedName("price")
        @Expose
        val price: String = "",

        @SerializedName("product_id")
        @Expose
        val productId: String = "",

        @SerializedName("rating")
        @Expose
        val rating: String = "",

        @SerializedName("returnable")
        @Expose
        val returnable: Boolean = false,

        @SerializedName("total_review")
        @Expose
        val totalReview: String = "",

        @SerializedName("uri")
        @Expose
        val uri: String = "",

        @SerializedName("wholesale")
        @Expose
        val wholesale: Boolean = false,

        @SerializedName("free_ongkir")
        @Expose
        val freeOngkir: FreeOngkir = FreeOngkir(),

        @SerializedName("label_groups")
        @Expose
        val labelGroupList: List<LabelGroup> = listOf()
){

        data class ShopFeaturedProductList(
                @SerializedName("data")
                @Expose
                val `data`: List<ShopFeaturedProduct> = listOf()
        )

        data class Response(
                @SerializedName("shop_featured_product")
                @Expose
                val shopFeaturedProductList: ShopFeaturedProductList = ShopFeaturedProductList()
        )


}