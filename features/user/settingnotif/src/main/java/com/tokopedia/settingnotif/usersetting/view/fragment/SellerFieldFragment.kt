package com.tokopedia.settingnotif.usersetting.view.fragment

import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.view.fragment.base.SettingFieldFragment

class SellerFieldFragment: SettingFieldFragment() {

    override fun getGqlRawQuery(): Int {
        return R.raw.query_seller_notif_setting
    }

    override fun getScreenName() = getString(R.string.settingnotif_seller)
    override fun getNotificationType() = SELLER_NOTIF_TYPE

}