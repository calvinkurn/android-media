package com.tokopedia.tkpd.home.wishlist.adapter.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tkpd.home.wishlist.adapter.factory.WishlistTypeFactory
import com.tokopedia.topads.sdk.domain.model.TopAdsModel

/**
 * Author errysuprayogi on 03,July,2019
 */
class WishlistTopAdsViewModel(val topAdsModel: TopAdsModel, q: String) : Visitable<WishlistTypeFactory> {

    var query: String = ""

    init {
        query = q
    }

    override fun type(typeFactory: WishlistTypeFactory): Int {
        return typeFactory.type(this)
    }
}
