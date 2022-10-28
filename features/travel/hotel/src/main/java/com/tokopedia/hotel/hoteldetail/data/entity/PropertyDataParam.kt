package com.tokopedia.hotel.hoteldetail.data.entity

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.hotel.common.data.HotelSourceEnum

/**
 * @author by furqan on 26/04/19
 */
class PropertyDataParam(
                        @SuppressLint("Invalid Data Type")
                        @SerializedName("propertyId")
                        @Expose
                        val propertyId: Long = 0,

                        @SerializedName("source")
                        @Expose
                        val source: String = HotelSourceEnum.SEARCHRESULT.value)