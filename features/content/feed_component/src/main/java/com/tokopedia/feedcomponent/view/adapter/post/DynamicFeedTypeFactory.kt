package com.tokopedia.feedcomponent.view.adapter.post

import com.tokopedia.feedcomponent.view.viewmodel.banner.BannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TopAdsBannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.highlight.HighlightViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.recommendation.FeedRecommendationViewModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopUiModel

/**
 * @author by milhamj on 03/12/18.
 */
interface DynamicFeedTypeFactory {
    fun type(dynamicPostViewModel: DynamicPostViewModel): Int

    fun type(feedRecommendationViewModel: FeedRecommendationViewModel): Int

    fun type(bannerViewModel: BannerViewModel): Int

    fun type(topadsShopUiModel: TopadsShopUiModel): Int

    fun type(topadsHeadlineUiModel: TopadsHeadlineUiModel): Int

    fun type(highlightViewModel: HighlightViewModel): Int

    fun type(topAdsBannerViewmodel: TopAdsBannerViewModel): Int

    fun type(carouselPlayCardViewModel: CarouselPlayCardViewModel): Int
}