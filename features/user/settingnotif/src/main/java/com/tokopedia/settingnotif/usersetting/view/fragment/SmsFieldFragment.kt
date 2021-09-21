package com.tokopedia.settingnotif.usersetting.view.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.data.pojo.SmsSection
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.VisitableSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.ChangeItemDataView.changePhoneNumber
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationPhoneNumber
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment

class SmsFieldFragment: SettingFieldFragment() {

    override fun getGqlRawQuery(): Int {
        return R.raw.query_sms_setting
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isRequestData = false
    }

    override fun onSuccessGetUserSetting(data: UserSettingDataView) {
        val pinnedData = arrayListOf<VisitableSettings>()
        if (userSession.phoneNumber.isEmpty()) {
            /*
            * showing pinned message to
            * instruction to add a new phone number
            * */
            pinnedData.add(activationPhoneNumber())
        } else {
            /*
            * if user has a phone number,
            * show the phone number changer card and
            * SMS ticker layout
            * */
            pinnedData.add(changePhoneNumber(userSession.phoneNumber))
            pinnedData.add(SmsSection())
        }
        data.data = pinnedData.toList()
        super.onSuccessGetUserSetting(data)
    }

    override fun getScreenName() = getString(R.string.settingnotif_sms)
    override fun getNotificationType() = TYPE_SMS

}