package com.tokopedia.sellerhomedrawer.presentation.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhomedrawer.presentation.view.adapter.SellerDrawerAdapterTypeFactory

open class SellerDrawerItem (
        var label: String = "",
        var iconId: Int = 0,
        var notif: Int = 0,
        var isExpanded: Boolean = false,
        var id: Int,
        var position: Int? = null,
        var isNew: Boolean = false,
        var isSelected: Boolean = false
) : Visitable<SellerDrawerAdapterTypeFactory> {

    override fun type(typeFactory: SellerDrawerAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}