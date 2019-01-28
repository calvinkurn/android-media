package com.tokopedia.feedcomponent.view.adapter.post

import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopViewModel

/**
 * @author by milhamj on 03/12/18.
 */
interface DynamicFeedTypeFactory {
    fun type(dynamicPostViewModel: DynamicPostViewModel): Int

    fun type(feedRecommendationViewModel: FeedRecommendationViewModel): Int

    fun type(bannerViewModel: BannerViewModel): Int

    fun type(topadsShopViewModel: TopadsShopViewModel): Int
}