package com.tokopedia.settingnotif.usersetting.view.viewmodel

import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.fragment.*
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment

data class SettingType(
        val icon: Int = 0,
        val name: Int = 0,
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
                            name = R.string.settingnotif_dialog_info_title,
                            fragment = PushNotifFieldFragment::class.java
                    ),
                    SettingType(
                            icon = R.drawable.ic_notifsetting_email,
                            name = R.string.settingnotif_email,
                            fragment = EmailFieldFragment::class.java
                    ),
                    SettingType(
                            icon = R.drawable.ic_notifsetting_sms,
                            name = R.string.settingnotif_sms,
                            fragment = SmsFieldFragment::class.java
                    )
            )
        }

        fun createSellerType(): SettingType {
            return SettingType(
                    icon = 0,
                    name = R.string.settingnotif_seller,
                    fragment = SellerFieldFragment::class.java
            )
        }
    }
}