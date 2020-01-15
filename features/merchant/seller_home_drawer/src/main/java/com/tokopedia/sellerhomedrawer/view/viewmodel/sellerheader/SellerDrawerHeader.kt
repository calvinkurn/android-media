package com.tokopedia.sellerhomedrawer.view.viewmodel.sellerheader

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhomedrawer.view.adapter.SellerDrawerAdapterTypeFactory
import com.tokopedia.sellerhomedrawer.view.viewmodel.BaseDrawerHeader
import com.tokopedia.sellerhomedrawer.view.viewmodel.header.SellerDrawerNotification
import com.tokopedia.sellerhomedrawer.view.viewmodel.header.SellerDrawerProfile

class SellerDrawerHeader : BaseDrawerHeader, Visitable<SellerDrawerAdapterTypeFactory> {

    constructor(): super()

    constructor(sellerDrawerProfile: SellerDrawerProfile, sellerDrawerNotification: SellerDrawerNotification):
            super(sellerDrawerProfile, sellerDrawerNotification)

    override fun type(typeFactory: SellerDrawerAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}