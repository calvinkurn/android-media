package com.tokopedia.troubleshooter.notification.ui.uiview

sealed class RingtoneState {
    object Normal: RingtoneState()
    object Vibrate: RingtoneState()
    object Silent: RingtoneState()
}