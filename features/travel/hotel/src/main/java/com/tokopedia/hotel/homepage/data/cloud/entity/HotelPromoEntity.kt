package com.tokopedia.hotel.homepage.data.cloud.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 10/04/19
 */
class HotelPromoData(
        @SerializedName("travelBanner")
        @Expose
        val travelBanner: List<HotelPromoEntity> = arrayListOf()
)

class HotelPromoEntity (
        @SerializedName("id")
        @Expose
        val promoId: String = "",
        @SerializedName("attributes")
        @Expose
        val attributes: HotelPromoAttributesEntity = HotelPromoAttributesEntity()
)