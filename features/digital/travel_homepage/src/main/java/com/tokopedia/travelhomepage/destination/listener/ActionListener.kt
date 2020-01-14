package com.tokopedia.travelhomepage.destination.listener

/**
 * @author by jessica on 2020-01-03
 */

interface ActionListener {

    fun clickAndRedirect(appUrl: String, webUrl: String = "")

    fun onTrackDestinationSection(firstVisiblePosition: Int, lastVisiblePosition: Int)

    fun onTrackOrderListImpression(firstVisiblePosition: Int, lastVisiblePosition: Int)

    fun onTrackOrderClick(position: Int)

    fun onTrackRecommendationsImpression()

    fun onTrackRecommendationItemClick(position: Int)

    fun onTrackEventsImpression(firstVisiblePosition: Int, lastVisiblePosition: Int)

    fun onTrackEventItemClick(position: Int)

    fun onTrackEventClickSeeAll()

    fun onTrackDealsImpression(firstVisiblePosition: Int, lastVisiblePosition: Int)

    fun onTrackDealsItemClick(position: Int)

    fun onTrackDeaksClickSeeAll()

    fun onTrackArticleImpression(firstVisiblePosition: Int, lastVisiblePosition: Int)

    fun onTrackArticleItemClick(position: Int)

    fun onTrackArticleClickSeeAll()
}
