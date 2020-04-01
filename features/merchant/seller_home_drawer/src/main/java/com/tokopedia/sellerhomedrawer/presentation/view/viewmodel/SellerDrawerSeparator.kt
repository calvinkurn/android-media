package com.tokopedia.sellerhomedrawer.presentation.view.viewmodel

import com.tokopedia.sellerhomedrawer.presentation.view.adapter.SellerDrawerAdapterTypeFactory

class SellerDrawerSeparator : SellerDrawerItem {

    constructor() : super(label = "", iconId = 0, id = 0, isExpanded = false)

    constructor(isExpanded: Boolean) : super(label = "", iconId = 0, id = 0, isExpanded = isExpanded)

    override fun type(typeFactory: SellerDrawerAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}