package com.tokopedia.smartbills.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DueUrgency(
        @SerializedName("Type")
        @Expose
        val type: Int = 0,
        @SerializedName("Text")
        @Expose
        val text: String = ""
)