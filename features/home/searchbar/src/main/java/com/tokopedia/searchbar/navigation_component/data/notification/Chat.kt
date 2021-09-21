package com.tokopedia.searchbar.navigation_component.data.notification


import com.google.gson.annotations.SerializedName

data class Chat(
    @SerializedName("unreads")
    val unreads: Int = 0,
    @SerializedName("unreadsSeller")
    val unreadsSeller: Int = 0,
    @SerializedName("unreadsUser")
    val unreadsUser: Int = 0
)