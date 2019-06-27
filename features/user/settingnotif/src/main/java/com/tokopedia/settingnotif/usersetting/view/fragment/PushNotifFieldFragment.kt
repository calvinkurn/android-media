package com.tokopedia.settingnotif.usersetting.view.fragment

import com.tokopedia.settingnotif.R

class PushNotifFieldFragment : SettingFieldFragment() {

    override fun getScreenName(): String = "Push Notification"

    override fun getGqlRawQuery(): Int {
        return R.raw.query_push_notif_setting
    }

}