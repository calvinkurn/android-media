package com.tokopedia.travelhomepage.homepage.presentation.listener

/**
 * @author by jessica on 2019-08-12
 */

interface OnItemBindListener {
    fun onBannerVHItemBind(isFromCloud: Boolean?)
    fun onCategoryVHBind(isFromCloud: Boolean?)
    fun onDestinationVHBind(isFromCloud: Boolean?)
    fun onOrderListVHBind(isFromCloud: Boolean?)
    fun onRecentSearchVHBind(isFromCloud: Boolean?)
    fun onRecommendationVHBind(isFromCloud: Boolean?)
}