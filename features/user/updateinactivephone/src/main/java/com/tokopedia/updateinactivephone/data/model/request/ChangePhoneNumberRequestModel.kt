package com.tokopedia.updateinactivephone.data.model.request

data class ChangePhoneNumberRequestModel (
    var isSuccess: Boolean = false,
    var errorMessage: String? = null,
    var statusMessage: String? = null,
    var responseCode: Int = 0,
    var uploadIdImageModel: UploadImageModel? = null,
    var uploadBankBookImageModel: UploadImageModel? = null,
    var submitImageModel: SubmitImageModel? = null,
    var uploadHostModel: UploadHostModel? = null,
    var isResponseSuccess: Boolean = false
)
