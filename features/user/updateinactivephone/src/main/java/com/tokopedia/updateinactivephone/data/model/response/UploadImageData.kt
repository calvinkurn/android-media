package com.tokopedia.updateinactivephone.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UploadImageData (
    @SerializedName("picture_obj")
    @Expose
    var picObj: String = ""
)
