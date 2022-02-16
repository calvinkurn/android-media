package com.tokopedia.chat_common.domain.pojo.imageupload

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 5/15/18.
 */
class ImageUploadAttributes {

    @SerializedName("image_url")
    @Expose
    val imageUrl: String = ""
    @SerializedName("image_url_thumbnail")
    @Expose
    val thumbnail: String = ""
}
