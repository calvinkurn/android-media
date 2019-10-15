package com.tokopedia.settingnotif.usersetting.view.fragment

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.PushNotifierTroubleshooterSetting
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingFieldTypeFactory
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel

class PushNotifFieldFragment : SettingFieldFragment() {

    private val txtButton = "Push Notification Troubleshooting"
    override fun getScreenName(): String = "Push Notification"
    override fun getNotificationType(): String  = "pushnotif"

    override fun getGqlRawQuery(): Int {
        return R.raw.query_push_notif_setting
    }

    override fun onSuccessGetUserSetting(data: UserSettingViewModel) {
        val settingFieldTypeFactory = arrayListOf<Visitable<SettingFieldTypeFactory>>()
        settingFieldTypeFactory.addAll(data.data)
        settingFieldTypeFactory.add(PushNotifierTroubleshooterSetting(txtButton))
        data.data = settingFieldTypeFactory
        super.onSuccessGetUserSetting(data)
    }

}