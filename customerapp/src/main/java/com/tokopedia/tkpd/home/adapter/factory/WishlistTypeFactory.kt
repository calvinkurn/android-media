package com.tokopedia.tkpd.home.adapter.factory

import com.tokopedia.tkpd.home.adapter.viewholder.WishListTopAdsViewHolder
import com.tokopedia.tkpd.home.adapter.viewmodel.WishlistEmptyViewModel
import com.tokopedia.tkpd.home.adapter.viewmodel.WishlistProductViewModel
import com.tokopedia.tkpd.home.adapter.viewmodel.WishlistTopAdsViewModel

/**
 * Author errysuprayogi on 03,July,2019
 */
interface WishlistTypeFactory {

    fun type(viewModel: WishlistProductViewModel): Int

    fun type(viewModel: WishlistEmptyViewModel): Int

    fun type(viewModel: WishlistTopAdsViewModel): Int

}
