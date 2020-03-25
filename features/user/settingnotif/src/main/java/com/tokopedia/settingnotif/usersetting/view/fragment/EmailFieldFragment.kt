package com.tokopedia.settingnotif.usersetting.view.fragment

import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment

class EmailFieldFragment: SettingFieldFragment() {

    override fun getScreenName(): String {
        return getString(R.string.settingnotif_email)
    }

    override fun getNotificationType(): String {
        return getString(R.string.settingnotif_email)
    }

    override fun getGqlRawQuery(): Int {
        return R.raw.query_email_setting
    }

}