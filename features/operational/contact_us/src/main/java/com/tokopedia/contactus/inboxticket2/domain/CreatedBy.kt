package com.tokopedia.contactus.inboxticket2.domain

import com.google.gson.annotations.SerializedName

data class CreatedBy(
        @SerializedName("role")
        val role: String? = null,
        @SerializedName("name")
        var name: String? = null,
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("picture")
        val picture: String? = null
)
