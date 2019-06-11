package com.tokopedia.settingnotif.usersetting.view.viewmodel

data class SettingType(
        val name: String
) {

    companion object {
        fun createSettingTypes(): List<SettingType> {
            return arrayListOf(
                    SettingType("Push Notification"),
                    SettingType("Email"),
                    SettingType("SMS")
            )
        }
    }
}