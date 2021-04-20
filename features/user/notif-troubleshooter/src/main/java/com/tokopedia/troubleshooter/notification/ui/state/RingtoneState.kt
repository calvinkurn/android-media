package com.tokopedia.troubleshooter.notification.ui.state

sealed class RingtoneState {
    object Normal: RingtoneState()
    object Vibrate: RingtoneState()
    object Silent: RingtoneState()

    companion object {
        fun isSilent(state: RingtoneState?): Boolean {
            return state == Silent || state == Vibrate
        }
    }
}