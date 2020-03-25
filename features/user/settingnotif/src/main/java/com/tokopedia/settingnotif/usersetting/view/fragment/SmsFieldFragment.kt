package com.tokopedia.settingnotif.usersetting.view.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.SmsSection
import com.tokopedia.settingnotif.usersetting.view.viewmodel.UserSettingViewModel

class SmsFieldFragment: SettingFieldFragment() {

    override fun getScreenName(): String = "SMS"
    override fun getNotificationType(): String  = "sms"

    override fun getGqlRawQuery(): Int {
        return R.raw.query_sms_setting
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isRequestData = false
    }

    override fun onSuccessGetUserSetting(data: UserSettingViewModel) {
        val newData = arrayListOf<VisitableSettings>()
        newData.add(SmsSection())
        data.data = newData.toList()
        super.onSuccessGetUserSetting(data)
    }

}