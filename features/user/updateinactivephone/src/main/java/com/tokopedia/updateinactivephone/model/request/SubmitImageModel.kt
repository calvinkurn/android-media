package com.tokopedia.updateinactivephone.model.request

data class SubmitImageModel (
    var isSuccess: Boolean = false,
    var errorMessage: String? = "",
    var statusMessage: String? = "",
    var responseCode: Int = 0,
    val isResponseSuccess: Boolean = false


)
