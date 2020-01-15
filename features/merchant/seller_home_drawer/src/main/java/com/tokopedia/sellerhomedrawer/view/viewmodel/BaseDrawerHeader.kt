package com.tokopedia.sellerhomedrawer.view.viewmodel

import com.tokopedia.loyalty.common.TokoPointDrawerData
import com.tokopedia.sellerhomedrawer.view.viewmodel.header.*

open class BaseDrawerHeader {

    constructor() {
        sellerDrawerDeposit = SellerDrawerDeposit()
        sellerDrawerProfile = SellerDrawerProfile()
        sellerDrawerTokoCash = SellerDrawerTokoCash()
        sellerDrawerNotification = SellerDrawerNotification()
        sellerDrawerTopPoints = SellerDrawerTopPoints()
    }

    constructor(sellerDrawerProfile: SellerDrawerProfile, sellerDrawerNotification: SellerDrawerNotification) {
        this.sellerDrawerNotification = sellerDrawerNotification
        this.sellerDrawerProfile = sellerDrawerProfile
    }

    var sellerDrawerProfile: SellerDrawerProfile
    var sellerDrawerNotification: SellerDrawerNotification
    var sellerDrawerTokoCash: SellerDrawerTokoCash? = null
    var sellerDrawerTopPoints: SellerDrawerTopPoints? = null
    var sellerDrawerDeposit: SellerDrawerDeposit? = null
    var tokoPointDrawerData: TokoPointDrawerData? = null
    var profileCompletion: Int = 0

}