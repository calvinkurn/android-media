package com.tokopedia.utils.aidl.data

import com.tokopedia.config.GlobalConfig

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