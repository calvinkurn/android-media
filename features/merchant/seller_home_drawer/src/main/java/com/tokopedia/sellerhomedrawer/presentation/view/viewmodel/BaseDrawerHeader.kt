package com.tokopedia.sellerhomedrawer.presentation.view.viewmodel

import com.tokopedia.sellerhomedrawer.data.SellerDrawerTokoCash
import com.tokopedia.sellerhomedrawer.data.header.*

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
    var tokoPointDrawerData: SellerTokoPointDrawerData? = null
    var profileCompletion: Int = 0

}