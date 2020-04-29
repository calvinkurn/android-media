package com.tokopedia.updateinactivephone.data.model.request

import com.tokopedia.updateinactivephone.data.model.response.UploadImageData

data class UploadImageModel (
    var isSuccess: Boolean = false,
    var uploadImageData: UploadImageData? = null,
    var errorMessage: String? = null,
    var responseCode: Int = 0
)
