package com.tokopedia.flight.bookingV2.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by furqan on 04/03/19
 */
class AddToCartEntity(
    @SerializedName("type")
    @Expose
    val type: String = "",
    @SerializedName("id")
    @Expose
    val id: String = "")