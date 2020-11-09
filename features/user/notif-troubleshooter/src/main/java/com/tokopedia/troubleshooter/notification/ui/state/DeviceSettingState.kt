package com.tokopedia.troubleshooter.notification.ui.state

sealed class DeviceSettingState {
    object None: DeviceSettingState()
    object High: DeviceSettingState()
    object Low: DeviceSettingState()
}