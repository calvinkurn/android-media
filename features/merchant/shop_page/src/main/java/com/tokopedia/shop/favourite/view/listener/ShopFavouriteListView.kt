package com.tokopedia.shop.favourite.view.listener

import com.tokopedia.abstraction.base.view.listener.BaseListViewListener
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.favourite.view.model.ShopFollowerUiModel

interface ShopFavouriteListView : BaseListViewListener<ShopFollowerUiModel?> {
    fun onSuccessGetShopInfo(shopInfo: ShopInfo?)
    fun onErrorToggleFavourite(throwable: Throwable?)
    fun onSuccessToggleFavourite(successValue: Boolean)
}