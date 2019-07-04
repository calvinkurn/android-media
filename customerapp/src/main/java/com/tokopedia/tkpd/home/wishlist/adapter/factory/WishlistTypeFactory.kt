package com.tokopedia.tkpd.home.wishlist.adapter.factory

import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.*

/**
 * Author errysuprayogi on 03,July,2019
 */
interface WishlistTypeFactory {

    fun type(viewModel: WishlistProductViewModel): Int

    fun type(viewModel: WishlistEmptyViewModel): Int

    fun type(viewModel: WishlistEmptySearchViewModel): Int

    fun type(viewModel: WishlistTopAdsViewModel): Int

    fun type(viewModel: WishlistRecomendationViewModel): Int

    fun type(viewModel: WishlistRecomTitleViewModel): Int

}
