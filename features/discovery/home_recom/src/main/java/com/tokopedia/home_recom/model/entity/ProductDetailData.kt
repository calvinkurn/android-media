package com.tokopedia.home_recom.model.entity


import com.google.gson.annotations.SerializedName

data class ProductDetailData(
        @SerializedName("app_url")
        val appUrl: String,
        @SerializedName("badges")
        val badges: List<Badge>,
        @SerializedName("category_breadcrumbs")
        val categoryBreadcrumbs: String,
        @SerializedName("click_url")
        val clickUrl: String,
        @SerializedName("count_review")
        val countReview: Int,
        @SerializedName("department_id")
        val departmentId: Int,
        @SerializedName("discount_percentage")
        val discountPercentage: Int,
        @SerializedName("id")
        val id: Int,
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("is_topads")
        val isTopads: Boolean,
        @SerializedName("is_wishlisted")
        val isWishlisted: Boolean,
        @SerializedName("labels")
        val labels: List<Label>,
        @SerializedName("name")
        val name: String,
        @SerializedName("price")
        val price: String,
        @SerializedName("price_int")
        val priceInt: Int,
        @SerializedName("product_key")
        val productKey: String,
        @SerializedName("rating")
        val rating: Int,
        @SerializedName("shop")
        val shop: Shop,
        @SerializedName("shop_domain")
        val shopDomain: String,
        @SerializedName("slashed_price")
        val slashedPrice: String,
        @SerializedName("slashed_price_int")
        val slashedPriceInt: Int,
        @SerializedName("stock")
        val stock: Int,
        @SerializedName("url")
        val url: String,
        @SerializedName("url_path")
        val urlPath: String,
        @SerializedName("wholesale_price")
        val wholesalePrice: List<Any>
)