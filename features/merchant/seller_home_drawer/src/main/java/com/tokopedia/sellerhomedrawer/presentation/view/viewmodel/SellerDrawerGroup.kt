package com.tokopedia.sellerhomedrawer.presentation.view.viewmodel

import com.tokopedia.sellerhomedrawer.presentation.view.adapter.SellerDrawerAdapterTypeFactory
import java.util.*

class SellerDrawerGroup : SellerDrawerItem {

    var list: MutableList<SellerDrawerItem> = ArrayList()

    constructor(label: String, iconId: Int, id: Int):
        super(label = label, iconId = iconId, id = id, isExpanded = false)


    constructor(label: String, iconId: Int, id: Int, isExpanded: Boolean, notif: Int):
        super(label = label, iconId = iconId, id = id, isExpanded = isExpanded, notif = notif)

    override fun type(typeFactory: SellerDrawerAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun add(sellerDrawerItem: SellerDrawerItem) {
        this.list.add(sellerDrawerItem)
    }

}