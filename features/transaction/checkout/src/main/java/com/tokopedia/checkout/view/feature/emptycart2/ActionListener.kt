package com.tokopedia.checkout.view.feature.emptycart2

import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist

/**
 * Created by Irfan Khoirul on 2019-05-23.
 */

interface ActionListener {

    fun onClearPromo(promoCode: String)

    fun onClickShopNow()

    fun onItemWishListClicked(wishlist: Wishlist, position: Int)

    fun onShowAllWishlist();
}