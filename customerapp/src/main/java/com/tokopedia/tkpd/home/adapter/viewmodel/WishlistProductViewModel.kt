package com.tokopedia.tkpd.home.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.core.`var`.ProductItem
import com.tokopedia.tkpd.home.adapter.factory.WishlistTypeFactory

/**
 * Author errysuprayogi on 03,July,2019
 */
class WishlistProductViewModel(var productItem: ProductItem) : Visitable<WishlistTypeFactory> {

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }
}
