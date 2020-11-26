package com.tokopedia.deals.home.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 19/06/20
 */

data class DealsGetHomeLayoutParam(
        @SerializedName("key")
        @Expose
        val key: String = "",

        @SerializedName("value")
        @Expose
        val value: String = ""
) {
    companion object {
        const val DEFAULT_LOCATION_TYPE_CITY = "city"
        const val PARAM_LOCATION_TYPE = "location_type"
        const val PARAM_COORDINATE = "coordinate"
    }
}