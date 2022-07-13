package com.tokopedia.feedcomponent.view.widget.shoprecom.listener

import com.tokopedia.feedcomponent.data.pojo.shoprecom.ShopRecomUiModelItem

/**
 * created by fachrizalmrsln on 07/07/22
 **/
interface ShopRecommendationCallback {
    fun onShopRecomCloseClicked(item: ShopRecomUiModelItem)
    fun onShopRecomFollowClicked(item: ShopRecomUiModelItem)
    fun onShopRecomItemClicked(appLink: String)
}