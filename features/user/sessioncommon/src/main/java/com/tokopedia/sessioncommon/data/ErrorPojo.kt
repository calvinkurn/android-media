package com.tokopedia.sessioncommon.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 28/05/19.
 */
data class Error(
        @SerializedName("name")
        @Expose
        var name: String = "",
        @SerializedName("message")
        @Expose
        var message: String = ""
){}