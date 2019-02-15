package com.tokopedia.groupchat.chatroom.domain.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author by yfsx on 18/12/18.
 */
data class OverlayMessageAssetPojo (
        @SerializedName("title")
        var title: String = "",
        @SerializedName("description")
        var description: String = "",
        @SerializedName("image_url")
        var imageUrl: String ="",
        @SerializedName("image_link")
        var imageLink: String = "",
        @SerializedName("btn_title")
        var btnTitle: String = "",
        @SerializedName("btn_link")
        var btnLink: String = ""
) {}
