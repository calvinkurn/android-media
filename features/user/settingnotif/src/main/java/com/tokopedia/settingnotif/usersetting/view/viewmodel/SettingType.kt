package com.tokopedia.settingnotif.usersetting.view.viewmodel

import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.fragment.*

data class SettingType(
        val icon: Int = 0,
        val name: String = "",
        val fragment: Class<out SettingFieldFragment>
) {

    fun createNewFragmentInstance(): SettingFieldFragment {
        return fragment.newInstance()
    }

    companion object {
        fun createSettingTypes(): List<SettingType> {
            return arrayListOf(
                    SettingType(
                            icon = R.drawable.ic_notifsetting_notification,
                            name = "Push Notification",
                            fragment = PushNotifFieldFragment::class.java
                    ),
                    SettingType(
                            icon = R.drawable.ic_notifsetting_email,
                            name = "Email",
                            fragment = EmailFieldFragment::class.java
                    ),
                    SettingType(
                            icon = R.drawable.ic_notifsetting_sms,
                            name = "SMS",
                            fragment = SmsFieldFragment::class.java
                    )
            )
        }

        fun createSellerType(): SettingType {
            return SettingType(
                    icon = 0,
                    name = "Seller",
                    fragment = SellerFieldFragment::class.java
            )
        }
    }
}