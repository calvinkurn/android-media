package com.tokopedia.developer_options.api

import com.google.gson.annotations.SerializedName

class ImageResponse (
        @SerializedName("self")
        var self: String = "",
        @SerializedName("id")
        var id: String = "",
        @SerializedName("filename")
        var filename: String = ""
)