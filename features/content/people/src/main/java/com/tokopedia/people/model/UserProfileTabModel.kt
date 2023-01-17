package com.tokopedia.people.model

import com.google.gson.annotations.SerializedName

data class UserProfileTabModel(
    @SerializedName("feedXProfileTabs")
    val feedXProfileTabs: FeedXProfileTabs,
)

data class FeedXProfileTabs(
    @SerializedName("data")
    val tabs: List<Tabs>,
    @SerializedName("meta")
    val meta: Meta,
)

data class Tabs(
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("key")
    val key: String,
    @SerializedName("position")
    val position: Int,
    @SerializedName("title")
    val title: String,
)

data class Meta(
    @SerializedName("selectedIndex")
    val selectedIndex: Int,
)
