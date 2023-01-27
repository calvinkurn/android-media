package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

import com.tokopedia.chatbot.chatbot2.data.uploadsecure.CheckUploadSecureResponse

sealed class CheckUploadSecureState {
    data class SuccessCheckUploadSecure(val data: CheckUploadSecureResponse) : CheckUploadSecureState()
    object HandleFailureCheckUploadSecure : CheckUploadSecureState()
}
