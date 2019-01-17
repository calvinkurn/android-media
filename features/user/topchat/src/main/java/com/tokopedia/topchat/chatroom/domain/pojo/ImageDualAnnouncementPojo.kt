package com.tokopedia.topchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 04/01/19
 */

class ImageDualAnnouncementPojo {

    @SerializedName("image_url")
    @Expose
    val imageUrl: String = ""
    @SerializedName("url")
    @Expose
    val url: String = ""
    @SerializedName("image_url_2")
    @Expose
    val imageUrl2: String = ""
    @SerializedName("url_2")
    @Expose
    val url2: String = ""
}

