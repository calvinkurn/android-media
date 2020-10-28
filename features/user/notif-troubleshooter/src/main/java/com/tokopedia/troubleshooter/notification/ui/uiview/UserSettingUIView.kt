package com.tokopedia.troubleshooter.notification.ui.uiview

data class UserSettingUIView(
        var totalOn: Int = 0,
        var notifications: Int = 0
) {
    companion object {
        fun merge(
                first: UserSettingUIView,
                second: UserSettingUIView
        ): UserSettingUIView {
            return UserSettingUIView().apply {
                totalOn = first.totalOn + second.totalOn
                notifications = first.notifications + second.notifications
            }
        }
    }
}