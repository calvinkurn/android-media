package com.tokopedia.travelhomepage.destination.listener

/**
 * @author by jessica on 2020-01-03
 */

interface OnViewHolderBindListener {
    fun onCitySummaryVHBind()
    fun onCitySummaryLoaded(imgUrls: List<String>, peekSize: Int, cityName: String)
    fun onOrderListVHBind()
    fun onCityRecommendationVHBind()
    fun onCityEventVHBind()
    fun onCityDealsVHBind()
    fun onCityArticleVHBind()
}