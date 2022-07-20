package com.tokopedia.feedcomponent.view.widget.shoprecom.listener

import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem

/**
 * created by fachrizalmrsln on 07/07/22
 **/
interface ShopRecommendationCallback {
    fun onShopRecomCloseClicked(itemID: Long)
    fun onShopRecomFollowClicked(itemID: Long)
    fun onShopRecomItemClicked(itemID: Long, appLink: String, imageUrl: String, postPosition: Int)

    fun onShopRecomItemImpress(item: ShopRecomUiModelItem, postPosition: Int)
}