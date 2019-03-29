package com.tokopedia.chat_common.domain.pojo.imageannouncement

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 04/01/19
 */

class ImageAnnouncementPojo {

    @SerializedName("image_url")
    @Expose
    val imageUrl: String = ""
    @SerializedName("url")
    @Expose
    val url: String = ""
}
