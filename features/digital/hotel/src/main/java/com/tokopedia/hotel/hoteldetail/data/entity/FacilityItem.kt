package com.tokopedia.hotel.hoteldetail.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class FacilityItem(@SerializedName("id")
                   @Expose
                   val id: Int = 0,
                   @SerializedName("name")
                   @Expose
                   val name: String = "",
                   @SerializedName("icon")
                   @Expose
                   val icon: String = "",
                   @SerializedName("availability")
                   @Expose
                   val availability: Int = 0)