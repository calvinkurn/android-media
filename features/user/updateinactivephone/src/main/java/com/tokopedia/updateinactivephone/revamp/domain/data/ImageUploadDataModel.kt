package com.tokopedia.updateinactivephone.revamp.domain.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ImageUploadDataModel (
    @SerializedName("picture_obj")
    @Expose
    var picObj: String = "",
    var source: String = ""
)
