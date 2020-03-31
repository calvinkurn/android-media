package com.tokopedia.settingnotif.usersetting.view.fragment

import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.VisitableSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.ChangeItemDataView.changeEmail
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationEmail
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingViewModel
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment

class EmailFieldFragment: SettingFieldFragment() {

    override fun getGqlRawQuery(): Int {
        return R.raw.query_email_setting
    }

    override fun onSuccessGetUserSetting(data: UserSettingViewModel) {
        val pinnedData = arrayListOf<VisitableSettings>()
        pinnedData.add(if (userSession.email.isNotEmpty()) {
            /*
            * showing change email card
            * is user has the email
            * */
            changeEmail(userSession.email)
        } else {
            /*
            * showing pinned message to
            * instruction to add a new email
            * */
            activationEmail()
        })
        pinnedData.addAll(data.data)
        data.data = pinnedData.toList()
        super.onSuccessGetUserSetting(data)
    }

    override fun getScreenName() = getString(R.string.settingnotif_email)
    override fun getNotificationType() = EMAIL_TYPE

}