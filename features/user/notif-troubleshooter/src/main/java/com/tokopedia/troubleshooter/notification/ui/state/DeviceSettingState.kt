package com.tokopedia.troubleshooter.notification.ui.state

sealed class DeviceSettingState {
    object Normal: DeviceSettingState()
    object High: DeviceSettingState()
    object Low: DeviceSettingState()

    companion object {
        fun isPriorityNormalOrHigh(state: DeviceSettingState): Boolean {
            return state == Normal || state == High
        }
    }
}