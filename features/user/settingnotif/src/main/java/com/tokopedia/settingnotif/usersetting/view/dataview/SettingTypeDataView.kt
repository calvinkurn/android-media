package com.tokopedia.settingnotif.usersetting.view.dataview

import com.tokopedia.config.GlobalConfig
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.fragment.EmailFieldFragment
import com.tokopedia.settingnotif.usersetting.view.fragment.PushNotifFieldFragment
import com.tokopedia.settingnotif.usersetting.view.fragment.SellerFieldFragment
import com.tokopedia.settingnotif.usersetting.view.fragment.SmsFieldFragment
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment

data class SettingTypeDataView(
        val icon: Int = 0,
        val iconUnify: Int = -1,
        val name: Int = 0,
        val fragment: Class<out SettingFieldFragment>
) {

    fun createNewFragmentInstance(): SettingFieldFragment {
        return fragment.newInstance()
    }

    companion object {
        fun createSettingTypes(): List<SettingTypeDataView> {
            return if (GlobalConfig.isSellerApp()) {
                arrayListOf(
                        createPushNotificationType(true),
                        SettingTypeDataView(
                                iconUnify = IconUnify.MESSAGE,
                                name = R.string.settingnotif_email,
                                fragment = EmailFieldFragment::class.java
                        )
                )
            } else {
                arrayListOf(
                        createPushNotificationType(false),
                        SettingTypeDataView(
                                icon = R.drawable.ic_notifsetting_email,
                                name = R.string.settingnotif_email,
                                fragment = EmailFieldFragment::class.java
                        ),
                        SettingTypeDataView(
                                icon = R.drawable.ic_notifsetting_sms,
                                name = R.string.settingnotif_sms,
                                fragment = SmsFieldFragment::class.java
                        )
                )
            }
        }

        fun createPushNotificationType(isSeller: Boolean = false): SettingTypeDataView {
            return if (isSeller) {
                SettingTypeDataView(
                        iconUnify = IconUnify.BELL,
                        name = R.string.settingnotif_seller,
                        fragment = SellerFieldFragment::class.java
                )
            } else {
                SettingTypeDataView(
                        icon = R.drawable.ic_notifsetting_notification,
                        name = R.string.settingnotif_dialog_info_title,
                        fragment = PushNotifFieldFragment::class.java
                )
            }
        }
    }
}