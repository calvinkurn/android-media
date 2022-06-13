package com.tokopedia.notifcenter.data.entity.notification


import com.google.gson.annotations.SerializedName

data class EmptyStateContent(
    @SerializedName("content")
    val content: String = "",
    @SerializedName("link")
    val link: Link = Link()
)