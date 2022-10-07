package com.tokopedia.feedcomponent.shoprecom.listener

import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomUiModelItem

/**
 * created by fachrizalmrsln on 07/07/22
 **/
interface ShopRecomCallback {
    fun onShopRecomCloseClicked(itemID: Long)
    fun onShopRecomFollowClicked(itemID: Long)
    fun onShopRecomItemClicked(itemID: Long, appLink: String, imageUrl: String, postPosition: Int)

    fun onShopRecomItemImpress(item: ShopRecomUiModelItem, postPosition: Int)
}
