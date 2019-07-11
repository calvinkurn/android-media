package com.tokopedia.profilecompletion.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * https://wiki.tokopedia.net/(Action)_Upload_Shop_Image_WSv4
 */

data class UploadProfileImageModel(@SerializedName("data")
                                    @Expose
                                    val data: Data? = null,
                                   @SerializedName("status")
                                    @Expose
                                    private val status: String? = null) {

    data class Data(@SerializedName("file_path")
                    @Expose
                    var filePath: String = "",
                    @SerializedName("file_th")
                    @Expose
                    var fileThumb: String = "",
                    @SerializedName("pic_obj")
                    @Expose
                    var picObj: String = "",
                    @SerializedName("success")
                    @Expose
                    var success: String = "0"
    )
}