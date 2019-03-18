package com.tokopedia.affiliate.feature.createpost.data.pojo.uploadvideo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.affiliate.feature.createpost.data.pojo.uploadimage.UploadImageData

/**
 * @author by nisie on 14/03/19.
 */

class UploadVideoResponse {
    @SerializedName("data")
    @Expose
    var data: UploadImageData = UploadImageData()
}