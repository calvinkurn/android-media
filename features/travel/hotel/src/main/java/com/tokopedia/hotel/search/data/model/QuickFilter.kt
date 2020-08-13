package com.tokopedia.hotel.search.data.model

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

        @SerializedName("value")
        @Expose
        val value: String = "",

        @SerializedName("selected")
        @Expose
        val selected: Boolean = false,

        var type: String = ""
)