package com.tokopedia.feedcomponent.data.feedrevamp


import com.google.gson.annotations.SerializedName

data class FeedXAuthor(
        @SerializedName("appLink")
        var appLink: String = "",
        @SerializedName("badgeURL")
        var badgeURL: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("id")
        var id: String = "",
        @SerializedName("logoURL")
        var logoURL: String = "",
        @SerializedName("name")
        var name: String = "",
        @SerializedName("type")
        var type: Int = 0,
        @SerializedName("webLink")
        var webLink: String = ""
)