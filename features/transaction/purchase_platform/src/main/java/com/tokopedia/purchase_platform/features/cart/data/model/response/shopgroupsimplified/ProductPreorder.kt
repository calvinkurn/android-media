package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class ProductPreorder(
    @SerializedName("duration_text")
    @Expose
    val durationText: String = "",
    @SerializedName("duration_day")
    @Expose
    val durationDay: Int = 0,
    @SerializedName("duration_unit_code")
    @Expose
    val durationUnitCode: Int = 0,
    @SerializedName("duration_unit_text")
    @Expose
    val durationUnitText: String = "",
    @SerializedName("duration_value")
    @Expose
    val durationValue: Int = 0
)