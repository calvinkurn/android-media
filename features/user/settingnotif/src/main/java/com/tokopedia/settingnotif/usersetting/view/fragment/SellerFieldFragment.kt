package com.tokopedia.settingnotif.usersetting.view.fragment

import com.tokopedia.settingnotif.R

class SellerFieldFragment: SettingFieldFragment() {

    override fun getScreenName(): String = "Seller"
    override fun getNotificationType(): String  = "seller"

    override fun getGqlRawQuery(): Int {
        return R.raw.query_seller_notif_setting
    }

}