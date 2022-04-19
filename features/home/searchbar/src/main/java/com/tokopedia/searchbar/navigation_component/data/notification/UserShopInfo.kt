package com.tokopedia.searchbar.navigation_component.data.notification


import com.google.gson.annotations.SerializedName

data class UserShopInfo(
    @SerializedName("info")
    val info: Info = Info()
)