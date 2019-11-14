package com.tokopedia.shop.favourite.view.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.shop.favourite.view.adapter.ShopFavouriteAdapterTypeFactory

/**
 * Created by nathan on 2/6/18.
 */

data class ShopFollowerUiModel(
        val id: String,
        val name: String,
        val imageUrl: String,
        val profileUrl: String
) : Visitable<ShopFavouriteAdapterTypeFactory> {

    override fun type(typeFactory: ShopFavouriteAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
