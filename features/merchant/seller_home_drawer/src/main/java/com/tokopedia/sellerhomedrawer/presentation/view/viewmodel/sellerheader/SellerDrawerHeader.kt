package com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.sellerheader

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhomedrawer.presentation.view.adapter.SellerDrawerAdapterTypeFactory
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.BaseDrawerHeader
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerNotification
import com.tokopedia.sellerhomedrawer.data.header.SellerDrawerProfile

class SellerDrawerHeader : BaseDrawerHeader, Visitable<SellerDrawerAdapterTypeFactory> {

    constructor(): super()

    constructor(sellerDrawerProfile: SellerDrawerProfile, sellerDrawerNotification: SellerDrawerNotification):
            super(sellerDrawerProfile, sellerDrawerNotification)

    override fun type(typeFactory: SellerDrawerAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}