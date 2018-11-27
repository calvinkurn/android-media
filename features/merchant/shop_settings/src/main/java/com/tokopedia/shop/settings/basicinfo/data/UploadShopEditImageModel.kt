package com.tokopedia.shop.settings.basicinfo.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * https://wiki.tokopedia.net/(Action)_Upload_Shop_Image_WSv4
 */

data class UploadShopEditImageModel(@SerializedName("data")
                                    @Expose
                                    val data: Data? = null,
                                    @SerializedName("status")
                                    @Expose
                                    private val status: String? = null) {

    data class Data(@SerializedName("image")
                    @Expose
                    var image: Image? = null)

    data class Image(@SerializedName("pic_code")
                     @Expose
                     val picCode: String? = null,
                     @SerializedName("pic_src")
                     @Expose
                     val picSrc: String? = null,
                     @SerializedName("success")
                     @Expose
                     val success: String? = null)
}
