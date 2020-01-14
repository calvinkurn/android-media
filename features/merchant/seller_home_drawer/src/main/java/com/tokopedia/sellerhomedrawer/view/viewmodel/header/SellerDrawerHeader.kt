package com.tokopedia.sellerhomedrawer.view.viewmodel.header

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.core.drawer2.data.viewmodel.*
import com.tokopedia.loyalty.common.TokoPointDrawerData
import com.tokopedia.sellerhomedrawer.view.adapter.SellerDrawerAdapterTypeFactory

class SellerDrawerHeader : Visitable<SellerDrawerAdapterTypeFactory>{

    constructor() {
        this.drawerDeposit = DrawerDeposit()
        this.drawerProfile = DrawerProfile()
        this.drawerTokoCash = DrawerTokoCash()
        this.drawerNotification = DrawerNotification()
        this.drawerTopPoints = DrawerTopPoints()
    }

    constructor(drawerProfile: DrawerProfile, drawerNotification: DrawerNotification) {
        this.drawerNotification = drawerNotification
        this.drawerProfile = drawerProfile
    }

    var drawerProfile: DrawerProfile
    var drawerNotification: DrawerNotification
    var drawerTokoCash: DrawerTokoCash? = null
    var drawerTopPoints: DrawerTopPoints? = null
    var drawerDeposit: DrawerDeposit? = null
    var tokoPointDrawerData: TokoPointDrawerData? = null
    var profileCompletion: Int = 0

    override fun type(typeFactory: SellerDrawerAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}