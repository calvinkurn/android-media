package com.tokopedia.settingnotif.usersetting.view.fragment

import com.tokopedia.settingnotif.R

class EmailFieldFragment: SettingFieldFragment() {

    override fun getScreenName(): String = "Email"

    override fun getGqlRawQuery(): Int {
        return R.raw.query_email_setting
    }

}