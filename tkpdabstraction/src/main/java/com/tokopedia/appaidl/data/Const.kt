package com.tokopedia.appaidl.data

import com.tokopedia.config.GlobalConfig

const val REMOTE_SERVICE = "com.tokopedia.appaidl.service.RemoteService"

const val CUSTOMER_APP = "com.tokopedia.tkpd"
const val SELLER_APP = "com.tokopedia.sellerapp"

/*
* componentTargetName:
* the package name of the receiver target.
*
* @example:
* if you want to send the data from MainApp -> SellerApp,
* the componentTargetName should be SellerApp (vice versa)
* */
fun componentTargetName(): String {
    return if (GlobalConfig.isSellerApp()) CUSTOMER_APP else SELLER_APP
}

/*
* AIDL Tag:
* to validate the source service.
* */
fun aidlTag(): String {
    return if (GlobalConfig.isSellerApp()) SELLER_APP else CUSTOMER_APP
}

/*
* collection of userSession key
* */
interface UserKey {
    companion object {
        const val NAME = "name"
        const val EMAIL = "email"
        const val IS_LOGIN = "is_login"
    }
}