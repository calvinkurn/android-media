package com.tokopedia.troubleshooter.notification.ui.uiview

sealed class StatusState {
    object Loading: StatusState()
    object Success: StatusState()
    object Warning: StatusState()
    object Error: StatusState()
}

fun isState(status: Boolean): StatusState {
    return if (status) StatusState.Success else StatusState.Error
}