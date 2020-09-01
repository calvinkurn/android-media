package com.tokopedia.troubleshooter.notification.ui.uiview

sealed class DeviceSettingState {
    object Normal: DeviceSettingState()
    object High: DeviceSettingState()
    object Low: DeviceSettingState()
}