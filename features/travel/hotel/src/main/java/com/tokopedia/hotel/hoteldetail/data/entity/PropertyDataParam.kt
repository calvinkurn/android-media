package com.tokopedia.hotel.hoteldetail.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class PropertyDataParam(@SerializedName("propertyId")
                        @Expose
                        val propertyId: Int = 0)