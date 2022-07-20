package com.tokopedia.chat_common.domain.pojo.imageupload

import com.google.gson.annotations.SerializedName

/**
 * @author by nisie on 5/15/18.
 */
class ImageUploadAttributes {

    @SerializedName("image_url")
    val imageUrl: String = ""
    @SerializedName("image_url_thumbnail")
    val thumbnail: String = ""
    @SerializedName("image_url_secure")
    val imageUrlSecure: String = ""
}
