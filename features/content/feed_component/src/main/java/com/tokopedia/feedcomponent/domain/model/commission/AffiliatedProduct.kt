package com.tokopedia.feedcomponent.domain.model.commission

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-11-22
 */
data class AffiliatedProduct(

        @SerializedName("id")
        val id: Int = 0,

        @SerializedName("image")
        val image: String = "",

        @SerializedName("name")
        val name: String = "",

        @SerializedName("isActive")
        val isActive: Boolean = false,

        @SerializedName("totalSold")
        val totalSold: String = "0",

        @SerializedName("totalClick")
        val totalClick: String = "0",

        @SerializedName("commission")
        val commission: String = "0",

        @SerializedName("productCommission")
        val productCommission: String = "0",

        @SerializedName("productRating")
        val productRating: Int = 0,

        @SerializedName("createPostAppLink")
        val createPostAppLink: String = "",

        @SerializedName("adID")
        val adID: Int = 0,

        @SerializedName("productID")
        val productID: Int = 0,

        @SerializedName("reviewCount")
        val reviewCount: Int = 0
)