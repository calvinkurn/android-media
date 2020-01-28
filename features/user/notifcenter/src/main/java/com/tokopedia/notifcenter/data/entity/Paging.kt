package com.tokopedia.notifcenter.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Paging(
        @SerializedName("has_next")
        @Expose
        val hasNext: Boolean = false,
        @SerializedName("has_prev")
        @Expose
        val hasPrev: Boolean = false
)