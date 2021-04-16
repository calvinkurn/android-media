package com.tokopedia.favorite.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.favorite.view.adapter.FavoriteTypeFactory

/**
 *
 * @author kulomady on 1/24/17.
 */
class FavoriteShopUiModel : Visitable<FavoriteTypeFactory> {
    var shopId: String? = null
    var shopAvatarImageUrl: String? = null
    var shopName: String? = null
    var shopLocation: String? = null
    var isFavoriteShop = false
    var badgeUrl: String? = null

    override fun type(typeFactory: FavoriteTypeFactory): Int {
        return typeFactory.type(this)
    }

}
