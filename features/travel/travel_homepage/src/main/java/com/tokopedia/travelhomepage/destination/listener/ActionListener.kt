package com.tokopedia.travelhomepage.destination.listener

import com.tokopedia.travelhomepage.destination.model.TravelArticleModel
import com.tokopedia.travelhomepage.destination.model.TravelDestinationSectionModel

/**
 * @author by jessica on 2020-01-03
 */

interface ActionListener {

    fun clickAndRedirect(appUrl: String, webUrl: String = "")

    fun onTrackOrderListImpression(list: List<TravelDestinationSectionModel.Item>, firstVisiblePosition: Int)

    fun onTrackOrderClick(item: TravelDestinationSectionModel.Item, position: Int)

    fun onTrackRecommendationsImpression(list: List<TravelDestinationSectionModel.Item>, firstVisiblePosition: Int)

    fun onTrackRecommendationItemClick(item: TravelDestinationSectionModel.Item, position: Int)

    fun onTrackEventsImpression(list: List<TravelDestinationSectionModel.Item>, firstVisiblePosition: Int)

    fun onTrackEventItemClick(item: TravelDestinationSectionModel.Item, position: Int)

    fun onTrackEventClickSeeAll()

    fun onTrackDealsImpression(list: List<TravelDestinationSectionModel.Item>, firstVisiblePosition: Int)

    fun onTrackDealsItemClick(item: TravelDestinationSectionModel.Item, position: Int)

    fun onTrackDealsClickSeeAll()

    fun onTrackArticleImpression(list: List<TravelArticleModel.Item>, firstVisiblePosition: Int)

    fun onTrackArticleItemClick(item: TravelArticleModel.Item, position: Int)

    fun onTrackArticleClickSeeAll()
}
