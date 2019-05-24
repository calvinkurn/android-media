package com.tokopedia.checkout.view.feature.emptycart2.uimodel

import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

data class WishlistItemUiModel(
        var wishlist: Wishlist = Wishlist()
)