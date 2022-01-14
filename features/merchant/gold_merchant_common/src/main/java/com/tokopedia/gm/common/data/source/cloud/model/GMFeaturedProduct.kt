package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GMFeaturedProduct (
    @SerializedName("parent_id")
    @Expose
    private val parentId: String? = null,

    @SerializedName("product_id")
    @Expose
    var productId: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("uri")
    @Expose
    var uri: String? = null,

    @SerializedName("price")
    @Expose
    var price: Double? = null,

    @SerializedName("image_uri")
    @Expose
    var imageUri: String? = null,

    @SerializedName("preorder")
    @Expose
    var isPreorder: Boolean = false,

    @SerializedName("returnable")
    @Expose
    val isReturnable: Boolean = false,

    @SerializedName("wholesale")
    @Expose
    var isWholesale: Boolean = false,

    @SerializedName("cashback")
    @Expose
    var isCashback: Boolean = false,

    @SerializedName("cashback_detail")
    @Expose
    val cashbackDetail: GMFeaturedCashBackDetail? = null,

    @SerializedName("labels")
    @Expose
    var labels: List<GMFeaturedLabel>? = null,

    @SerializedName("is_rated")
    @Expose
    private val isRated: Boolean = false,

    @SerializedName("rating")
    @Expose
    var rating: Double = 0.0,

    @SerializedName("total_review")
    @Expose
    var totalReview: String? = null
)