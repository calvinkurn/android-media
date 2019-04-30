package com.tokopedia.hotel.hoteldetail.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 26/04/19
 */
class PropertyPolicyData(@SerializedName("name")
                         @Expose
                         val name: String = "",
                         @SerializedName("content")
                         @Expose
                         val content: String = "",
                         @SerializedName("propertyPolicyId")
                         @Expose
                         val propertyPolicyId: String = "")