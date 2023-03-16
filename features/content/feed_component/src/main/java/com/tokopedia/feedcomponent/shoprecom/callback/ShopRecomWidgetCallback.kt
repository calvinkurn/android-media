package com.tokopedia.feedcomponent.shoprecom.callback

import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem

/**
 * created by fachrizalmrsln on 14/10/22
 **/
interface ShopRecomWidgetCallback {
    fun onShopRecomCloseClicked(item: ShopRecomUiModelItem)
    fun onShopRecomFollowClicked(item: ShopRecomUiModelItem)
    fun onShopRecomItemClicked(item: ShopRecomUiModelItem, postPosition: Int)

    fun onShopRecomItemImpress(item: ShopRecomUiModelItem, postPosition: Int)

    fun onShopRecomLoadingNextPage(nextCursor: String)
}
