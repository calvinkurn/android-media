package com.tokopedia.settingnotif.usersetting.view.dataview

import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.fragment.*
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment

data class SettingTypeDataView(
        val icon: Int = 0,
        val name: Int = 0,
        val fragment: Class<out SettingFieldFragment>
) {

    fun createNewFragmentInstance(): SettingFieldFragment {
        return fragment.newInstance()
    }

    companion object {
        fun createSettingTypes(): List<SettingTypeDataView> {
            return arrayListOf(
                    SettingTypeDataView(
                            icon = R.drawable.ic_notifsetting_notification,
                            name = R.string.settingnotif_dialog_info_title,
                            fragment = PushNotifFieldFragment::class.java
                    ),
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

        fun createSellerType(): SettingTypeDataView {
            return SettingTypeDataView(
                    fragment = SellerFieldFragment::class.java,
                    name = R.string.settingnotif_seller
            )
        }
    }
}