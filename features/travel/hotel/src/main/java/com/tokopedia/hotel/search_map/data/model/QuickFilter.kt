package com.tokopedia.hotel.search_map.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by jessica on 12/08/20
 */

data class QuickFilter(
        @SerializedName("name")
        @Expose
        val name: String = "",

        @SerializedName("displayName")
        @Expose
        val displayName: String = "",

        @SerializedName("values")
        @Expose
        val values: List<String> = listOf(),

        @SerializedName("selected")
        @Expose
        val selected: Boolean = false,

        var type: String = ""
)