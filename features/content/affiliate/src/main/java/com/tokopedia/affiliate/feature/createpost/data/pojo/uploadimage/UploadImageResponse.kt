package com.tokopedia.affiliate.feature.createpost.data.pojo.uploadimage

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UploadImageResponse {
    @SerializedName("data")
    @Expose
    var data: UploadImageData = UploadImageData()
}