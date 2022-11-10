package com.tokopedia.feedcomponent.view.adapter.post

import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TopAdsBannerViewModel
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostViewModel
import com.tokopedia.feedcomponent.view.viewmodel.shimmer.ShimmerUiModel
import com.tokopedia.feedcomponent.view.viewmodel.shoprecommendation.ShopRecomWidgetModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadLineV2Model
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopUiModel

/**
 * @author by milhamj on 03/12/18.
 */
interface DynamicFeedTypeFactory {
    fun type(dynamicPostViewModel: DynamicPostViewModel): Int

    fun type(topadsShopUiModel: TopadsShopUiModel): Int

    fun type(topadsHeadlineUiModel: TopadsHeadlineUiModel): Int

    fun type(topadsHeadlineUiModel: TopadsHeadLineV2Model): Int

    fun type(topAdsBannerViewmodel: TopAdsBannerViewModel): Int

    fun type(carouselPlayCardViewModel: CarouselPlayCardViewModel): Int

    fun type(shopRecomWidgetModel: ShopRecomWidgetModel): Int

    fun type(shimmerUiModel: ShimmerUiModel): Int

    fun type(dynamicPostUiModel: DynamicPostUiModel) : Int
}
