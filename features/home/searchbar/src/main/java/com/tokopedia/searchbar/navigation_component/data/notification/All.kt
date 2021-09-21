package com.tokopedia.searchbar.navigation_component.data.notification


import com.google.gson.annotations.SerializedName

data class All(
    @SerializedName("total_int")
    val totalInt: Int = 0,
    @SerializedName("notifcenter_int")
    val notifcenterInt: Int = 0
)