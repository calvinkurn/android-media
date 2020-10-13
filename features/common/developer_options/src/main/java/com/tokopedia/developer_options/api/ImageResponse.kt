package com.tokopedia.developer_options.api

import com.google.gson.annotations.SerializedName

class ImageResponse (
        @SerializedName("self")
        var self: String = "",
        @SerializedName("id")
        var id: String = "",
        @SerializedName("filename")
        var filename: String = "",
        @SerializedName("author")
        var author: Author = Author(),
        @SerializedName("created")
        var created: String = "",
        @SerializedName("size")
        var size: Int = 0,
        @SerializedName("mimeType")
        var mimeType: String = "",
        @SerializedName("content")
        var content: String = "",
        @SerializedName("thumbnail")
        var thumbnail: String = ""
)


data class Author(
        @SerializedName("self")
        var self: String = "",
        @SerializedName("accountId")
        var accountId: String = "",
        @SerializedName("emailAddress")
        var emailAddress: String = "",
        @SerializedName("avatarUrls")
        var avatarUrls: Avatar = Avatar(),
        @SerializedName("displayName")
        var displayName: String = "",
        @SerializedName("active")
        var active: Boolean = true,
        @SerializedName("timeZone")
        var timeZone: String = "",
        @SerializedName("accountType")
        var accountType: String = ""
)

data class Avatar(
        @SerializedName("48x48")
        var x48: String = "",
        @SerializedName("24x24")
        var x24: String = "",
        @SerializedName("16x16")
        var x16: String = "",
        @SerializedName("32x32")
        var x32: String = ""
)