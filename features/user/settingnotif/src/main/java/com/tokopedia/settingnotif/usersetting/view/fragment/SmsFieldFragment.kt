package com.tokopedia.settingnotif.usersetting.view.fragment

import com.tokopedia.settingnotif.R

class SmsFieldFragment: SettingFieldFragment() {

    override fun getScreenName(): String = "SMS"

    override fun getGqlRawQuery(): Int {
        return R.raw.query_push_notif_setting
    }

}