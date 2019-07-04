package com.tokopedia.tkpd.home.wishlist.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tkpd.home.wishlist.adapter.factory.WishlistTypeFactory

/**
 * Author errysuprayogi on 03,July,2019
 */
class WishlistEmptyViewModel : Visitable<WishlistTypeFactory> {

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }
}
