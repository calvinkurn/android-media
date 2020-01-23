package com.tokopedia.sellerhomedrawer.presentation.view.viewmodel

import com.tokopedia.loyalty.common.TokoPointDrawerData
import com.tokopedia.sellerhomedrawer.data.SellerDrawerTokoCash
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.header.SellerDrawerDeposit
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.header.SellerDrawerNotification
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.header.SellerDrawerProfile
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.header.SellerDrawerTopPoints

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