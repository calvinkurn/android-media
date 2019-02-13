package com.tokopedia.groupchat.chatroom.kotlin.domain.pojo.channelinfo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ListOfficial (
        @SerializedName("title")
        @Expose
        var title: String = "",
        @SerializedName("list_brands")
        @Expose
        var listBrands: List<ListBrand>? = null

) {}
