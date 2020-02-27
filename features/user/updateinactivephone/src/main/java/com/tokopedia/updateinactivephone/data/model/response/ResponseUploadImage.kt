package com.tokopedia.updateinactivephone.data.model.response


data class ResponseUploadImage (
    var isSuccess: Boolean = false,
    var uploadImageData: UploadImageData? = null,
    var errorMessage: String? = "",
    var statusMessage: String? = "",
    var responseCode: Int = 0,
    val isResponseSuccess: Boolean = false
)
