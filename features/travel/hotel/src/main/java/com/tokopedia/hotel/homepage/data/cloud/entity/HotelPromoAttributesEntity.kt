package com.tokopedia.hotel.homepage.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 10/04/19
 */
class HotelPromoAttributesEntity(
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("linkURL")
        @Expose
        val linkUrl: String = "",
        @SerializedName("imageURL")
        @Expose
        val imageUrl: String = "",
        @SerializedName("promoCode")
        @Expose
        val promoCode: String = ""
)