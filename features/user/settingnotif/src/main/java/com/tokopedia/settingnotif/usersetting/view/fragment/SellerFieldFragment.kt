package com.tokopedia.settingnotif.usersetting.view.fragment

import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.VisitableSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationPushNotif
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingViewModel
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment

class SellerFieldFragment: SettingFieldFragment() {

    override fun getGqlRawQuery(): Int {
        return R.raw.query_seller_notif_setting
    }

    override fun onSuccessGetUserSetting(data: UserSettingViewModel) {
        val pinnedData = arrayListOf<VisitableSettings>()
        /*
        * showing pinned message
        * if notification permission turn off
        * */
        if (isNotificationEnabled() == false) {
            pinnedData.add(activationPushNotif())
        }
        pinnedData.addAll(data.data)
        data.data = pinnedData.toList()
        super.onSuccessGetUserSetting(data)
    }

    override fun getScreenName() = getString(R.string.settingnotif_settings_seller_title)
    override fun getNotificationType() = SELLER_NOTIF_TYPE

}