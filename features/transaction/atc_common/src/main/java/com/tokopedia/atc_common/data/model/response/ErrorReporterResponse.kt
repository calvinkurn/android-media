package com.tokopedia.atc_common.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ErrorReporterResponse(

        @SerializedName("eligible")
        @Expose
        var eligible: Boolean = false,

        @SerializedName("description")
        @Expose
        var description: String = ""
)