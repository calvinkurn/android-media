package com.tokopedia.chat_common.domain.pojo.imageannouncement

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 04/01/19
 */

class ImageAnnouncementPojo {

    @SerializedName("image_url")
    @Expose
    var imageUrl: String = ""
    @SerializedName("url")
    @Expose
    var url: String = ""
    @SerializedName("is_hide_banner")
    @Expose
    var isHideBanner: Boolean = false
}
