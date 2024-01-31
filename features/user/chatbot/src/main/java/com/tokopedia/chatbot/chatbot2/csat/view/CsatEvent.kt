package com.tokopedia.chatbot.chatbot2.csat.view

sealed interface CsatEvent {
    object NavigateToPreviousPage : CsatEvent
    object FallbackDismissBottomSheet : CsatEvent
    data class UpdateButton(val isEnabled: Boolean) : CsatEvent
    data class ShowError(val errorMessage: String) : CsatEvent
}
