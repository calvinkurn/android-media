package com.tokopedia.searchbar.navigation_component.data.notification


import com.google.gson.annotations.SerializedName

data class Inbox(
    @SerializedName("review")
    val review: Int = 0,
    @SerializedName("talk")
    val talk: Int = 0,
    @SerializedName("ticket")
    val ticket: Int = 0
)