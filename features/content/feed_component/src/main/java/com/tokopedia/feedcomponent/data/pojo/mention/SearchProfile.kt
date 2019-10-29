package com.tokopedia.feedcomponent.data.pojo.mention

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 2019-08-05.
 */

data class SearchProfile(
        @SerializedName("profiles")
        @Expose
        val profiles: List<Profile> = emptyList(),

        @SerializedName("query")
        @Expose
        val query: String = "",

        @SerializedName("has_next")
        @Expose
        val hasNext: Boolean = false,

        @SerializedName("count")
        @Expose
        val count: Int = 0
)