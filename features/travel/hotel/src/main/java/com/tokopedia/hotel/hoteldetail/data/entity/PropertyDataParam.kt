package com.tokopedia.hotel.hoteldetail.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.hotel.common.data.HotelSourceEnum

/**
 * @author by furqan on 26/04/19
 */
class PropertyDataParam(@SerializedName("propertyId")
                        @Expose
                        val propertyId: Long = 0,

                        @SerializedName("source")
                        @Expose
                        val source: String = HotelSourceEnum.SEARCHRESULT.value)