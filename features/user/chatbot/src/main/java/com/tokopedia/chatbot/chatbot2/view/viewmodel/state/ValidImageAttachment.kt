package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

sealed class ValidImageAttachment {
    object UnderSizedImage : ValidImageAttachment()
    object OverSizedImage : ValidImageAttachment()
    object CorrectImage : ValidImageAttachment()
    object OtherError : ValidImageAttachment()
}
