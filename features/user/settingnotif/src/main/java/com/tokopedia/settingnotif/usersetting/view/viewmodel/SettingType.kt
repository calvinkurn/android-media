package com.tokopedia.settingnotif.usersetting.view.viewmodel

import com.tokopedia.settingnotif.usersetting.view.fragment.EmailFieldFragment
import com.tokopedia.settingnotif.usersetting.view.fragment.PushNotifFieldFragment
import com.tokopedia.settingnotif.usersetting.view.fragment.SettingFieldFragment
import com.tokopedia.settingnotif.usersetting.view.fragment.SmsFieldFragment

data class SettingType(
        val name: String,
        val settingFieldFragment: Class<out SettingFieldFragment>
) {

    fun createNewFragmentInstance(): SettingFieldFragment {
        return settingFieldFragment.newInstance()
    }

    fun isPushNotificationFieldFragment(): Boolean {
        return settingFieldFragment == PushNotifFieldFragment::class.java
    }

    companion object {
        fun createSettingTypes(): List<SettingType> {
            return arrayListOf(
                    SettingType("Push Notification", PushNotifFieldFragment::class.java),
                    SettingType("Email", EmailFieldFragment::class.java),
                    SettingType("SMS", SmsFieldFragment::class.java)
            )
        }
    }
}