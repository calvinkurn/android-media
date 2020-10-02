package com.tokopedia.troubleshooter.notification.ui.state

sealed class StatusState {
    object Loading: StatusState()
    object Success: StatusState()
    object Warning: StatusState()
    object Error: StatusState()

    companion object {
        operator fun invoke(status: Boolean): StatusState {
            return if (status) Success else Error
        }
    }
}