package com.tokopedia.searchbar.navigation_component.data.notification


import com.google.gson.annotations.SerializedName

data class InboxCounter(
    @SerializedName("all")
    val all: All = All()
)