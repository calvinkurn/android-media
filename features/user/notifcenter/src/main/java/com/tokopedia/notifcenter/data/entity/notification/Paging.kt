package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class Paging(
        @SerializedName("has_next")
        val hasNext: Boolean = false,
        @SerializedName("has_prev")
        val hasPrev: Boolean = false
) {
    var lastNotifId = ""
}