package com.tokopedia.hotel.hoteldetail.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class RegionData(@SerializedName("id")
                 @Expose
                 val id: Int = 0,
                 @SerializedName("name")
                 @Expose
                 val name: String = "",
                 @SerializedName("countryName")
                 @Expose
                 val countryName: String = "")