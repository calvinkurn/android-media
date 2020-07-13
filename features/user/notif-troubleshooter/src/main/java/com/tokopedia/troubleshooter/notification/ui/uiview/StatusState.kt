package com.tokopedia.troubleshooter.notification.ui.uiview

sealed class StatusState {
    object Loading: StatusState()
    object Success: StatusState()
    object Error: StatusState()
}