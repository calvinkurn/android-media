package com.tokopedia.settingnotif.usersetting.view.fragment

import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.adapter.factory.VisitableSettings
import com.tokopedia.settingnotif.usersetting.view.dataview.NotificationActivationDataView.activationPushNotif
import com.tokopedia.settingnotif.usersetting.view.dataview.UserSettingDataView
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment

class SellerFieldFragment: SettingFieldFragment() {

    override fun getGqlRawQuery(): Int {
        return R.raw.query_seller_notif_setting
    }

    override fun onSuccessGetUserSetting(data: UserSettingDataView) {
        val pinnedData = arrayListOf<VisitableSettings>()
        /*
        * showing pinned message
        * if notification permission turn off
        * */
        if (!isNotificationEnabled()) {
            pinnedData.add(activationPushNotif())
        }
        pinnedData.addAll(data.data)
        data.data = pinnedData.toList()
        super.onSuccessGetUserSetting(data)
    }

    /*
    * especially for seller push notif,
    * request update setting must be use `pushnotif`
    * as a notification key
    * */
    override fun requestUpdateUserSetting(
            notificationType: String,
            updatedSettingIds: List<Map<String, Any>>
    ) {
        settingViewModel.requestUpdateUserSetting(TYPE_PUSH_NOTIF, updatedSettingIds)
        settingViewModel.requestUpdateMoengageUserSetting(updatedSettingIds)
    }

    override fun getScreenName() = getString(R.string.settingnotif_settings_seller_title)
    override fun getNotificationType() = TYPE_SELLER_NOTIF

}