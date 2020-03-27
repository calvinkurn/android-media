package com.tokopedia.settingnotif.usersetting.view.fragment

import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.dataview.ChangeItemDataView.changeEmail
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationEmail
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingViewModel
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment

class EmailFieldFragment: SettingFieldFragment() {

    override fun getScreenName(): String {
        return getString(R.string.settingnotif_email)
    }

    override fun getNotificationType(): String {
        return "email"
    }

    override fun getGqlRawQuery(): Int {
        return R.raw.query_email_setting
    }

    override fun onSuccessGetUserSetting(data: UserSettingViewModel) {
        val dataSettings = arrayListOf<VisitableSettings>()
        if (userSession.email.isNotEmpty()) {
            dataSettings.add(changeEmail(userSession.email))
        } else {
            dataSettings.add(activationEmail())
        }
        dataSettings.addAll(data.data)
        data.data = dataSettings.toList()
        super.onSuccessGetUserSetting(data)
    }

}