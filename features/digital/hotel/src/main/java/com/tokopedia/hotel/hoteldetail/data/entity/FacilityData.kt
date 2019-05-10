package com.tokopedia.hotel.hoteldetail.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class FacilityData(@SerializedName("groupName")
                   @Expose
                   val groupName: String,
                   @SerializedName("item")
                   @Expose
                   val item: List<FacilityItem> = listOf())