package com.tokopedia.settingnotif.usersetting.domain.pojo

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactory

data class PushNotifierTroubleshooterSetting(
        val title: String = "Push Notification Troubleshooting",
        val applink: String = ApplinkConst.PUSHNOTIFCHECKER
) : Visitable<SettingFieldTypeFactory> {

    override fun type(typeFactory: SettingFieldTypeFactory): Int {
        return typeFactory.type(this)
    }

}