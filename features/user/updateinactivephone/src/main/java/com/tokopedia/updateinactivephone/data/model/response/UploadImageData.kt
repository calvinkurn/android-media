package com.tokopedia.updateinactivephone.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadImageData (
    @SerializedName("pic_obj")
    @Expose
    var picObj: String = "",

    @SerializedName("pic_src")
    @Expose
    var picSrc: String = ""
)
