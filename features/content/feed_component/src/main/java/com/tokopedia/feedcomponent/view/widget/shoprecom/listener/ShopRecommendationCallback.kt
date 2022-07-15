package com.tokopedia.feedcomponent.view.widget.shoprecom.listener

/**
 * created by fachrizalmrsln on 07/07/22
 **/
interface ShopRecommendationCallback {
    fun onShopRecomCloseClicked(itemID: Long)
    fun onShopRecomFollowClicked(itemID: Long)
    fun onShopRecomItemClicked(itemID: Long, appLink: String)
}