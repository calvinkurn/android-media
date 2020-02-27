package com.tokopedia.updateinactivephone.model.request


import com.tokopedia.updateinactivephone.model.response.UploadImageData

data class UploadImageModel (
    var isSuccess: Boolean = false,
    var uploadImageData: UploadImageData? = null,
    var errorMessage: String? = null,
    var responseCode: Int = 0,
    val isResponseSuccess: Boolean = false
)
