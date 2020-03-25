package com.tokopedia.settingnotif.usersetting.view.fragment

import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment

class SellerFieldFragment: SettingFieldFragment() {

    override fun getScreenName(): String {
        return getString(R.string.settingnotif_seller)
    }

    override fun getNotificationType(): String {
        return getString(R.string.settingnotif_seller)
    }

    override fun getGqlRawQuery(): Int {
        return R.raw.query_seller_notif_setting
    }

}