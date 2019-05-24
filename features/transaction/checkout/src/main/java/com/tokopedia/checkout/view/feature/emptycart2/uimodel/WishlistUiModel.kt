package com.tokopedia.checkout.view.feature.emptycart2.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.checkout.view.feature.emptycart2.adapter.EmptyCartAdapterTypeFactory
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

data class WishlistUiModel(
        var wishlistItems: List<Wishlist> = arrayListOf()
) : Visitable<EmptyCartAdapterTypeFactory> {

    override fun type(typeFactory: EmptyCartAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}