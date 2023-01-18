package com.tokopedia.contactus.inboxtickets.domain

import com.google.gson.annotations.SerializedName

data class CreateBy(
        @SerializedName("role")
        val role: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("id")
        var id: Long = 0,
        @SerializedName("picture")
        val picture: String? = null
)
