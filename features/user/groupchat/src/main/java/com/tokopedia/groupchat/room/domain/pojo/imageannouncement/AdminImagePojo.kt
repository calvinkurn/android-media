package com.tokopedia.groupchat.room.domain.pojo.imageannouncement

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.groupchat.room.domain.pojo.BaseGroupChatPojo

class AdminImagePojo : BaseGroupChatPojo() {

    @SerializedName("image_url")
    @Expose
    var imageUrl: String? = null
    @SerializedName("redirect_url")
    @Expose
    var redirectUrl: String? = null
    @SerializedName("alt_text")
    @Expose
    var altText: String? = null

}
