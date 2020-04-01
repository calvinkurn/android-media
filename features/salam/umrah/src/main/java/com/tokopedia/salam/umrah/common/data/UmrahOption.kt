package com.tokopedia.salam.umrah.common.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by M on 21/11/2019
 */
data class UmrahOption(
        @SerializedName("displayText")
        @Expose
        val displayText: String = "",
        @SerializedName("query")
        @Expose
        val query: String = "",
        @SerializedName("rangeDisplayText")
        @Expose
        val rangeDisplayText: String = "",
        @SerializedName("minimum")
        @Expose
        val minimum: Int = 0,
        @SerializedName("maximum")
        @Expose
        val maximum: Int = 0,
        var isSelected: Boolean = false
)