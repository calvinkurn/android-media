package com.tokopedia.hotel.hoteldetail.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class DistrictData(@SerializedName("id")
                   @Expose
                   val id: Int = 0,
                   @SerializedName("cityId")
                   @Expose
                   val cityId: Int = 0,
                   @SerializedName("latitude")
                   @Expose
                   val latitude: Double = 0.0,
                   @SerializedName("longitude")
                   @Expose
                   val longitude: Double = 0.0,
                   @SerializedName("name")
                   @Expose
                   val name: String = "")