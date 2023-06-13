package com.tokopedia.similarsearch.getsimilarproducts.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

internal data class Product(
        @SerializedName("id")
        @Expose
        val id: String = "",

        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("url")
        @Expose
        val url: String = "",

        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",

        @SerializedName("image_url_700")
        @Expose
        val imageUrl700: String = "",

        @SerializedName("price")
        @Expose
        val price: String = "",

        @SerializedName("shop")
        @Expose
        val shop: Shop = Shop(),

        @SerializedName("badges")
        @Expose
        val badgeList: List<Badge> = listOf(),

        @SerializedName("label_groups")
        @Expose
        val labelGroups: List<LabelGroups> = listOf(),

        @SerializedName("category_id")
        @Expose
        val categoryId: Int = 0,

        @SerializedName("category_name")
        @Expose
        val categoryName: String = "",

        @SerializedName("rating")
        @Expose
        val rating: Int = 0,

        @SerializedName("rating_average")
        @Expose
        val ratingAverage: String = "",

        @SerializedName("count_review")
        @Expose
        val countReview: Int = 0,

        @SerializedName("original_price")
        @Expose
        val originalPrice: String = "",

        @SerializedName("price_range")
        @Expose
        val priceRange: String = "",

        @SerializedName("discount_expired_time")
        @Expose
        val discountExpiredTime: String = "",

        @SerializedName("discount_start_time")
        @Expose
        val discountStartTime: String = "",

        @SerializedName("discount_percentage")
        @Expose
        val discountPercentage: Int = 0,

        @SerializedName("wishlist")
        @Expose
        var isWishlisted: Boolean = false,

        @SerializedName("min_order")
        @Expose
        val minOrder: Int = 0,

        @SerializedName("free_ongkir")
        @Expose
        val freeOngkir: FreeOngkirBadge = FreeOngkirBadge(),

        @SerializedName("applink")
        @Expose
        val applink: String = "",

        @SerializedName("label_group_variant")
        @Expose
        val labelGroupVariantList: List<LabelGroupVariant> = emptyList(),
) {

        var position: Int = 0
}
