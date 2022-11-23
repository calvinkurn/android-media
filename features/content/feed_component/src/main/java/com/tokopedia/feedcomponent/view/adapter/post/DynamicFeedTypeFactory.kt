package com.tokopedia.feedcomponent.view.adapter.post

import com.tokopedia.feedcomponent.shoprecom.model.ShopRecomWidgetModel
import com.tokopedia.feedcomponent.view.viewmodel.DynamicPostUiModel
import com.tokopedia.feedcomponent.view.viewmodel.banner.TopAdsBannerModel
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardModel
import com.tokopedia.feedcomponent.view.viewmodel.post.DynamicPostModel
import com.tokopedia.feedcomponent.view.viewmodel.shimmer.ShimmerUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadLineV2Model
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsHeadlineUiModel
import com.tokopedia.feedcomponent.view.viewmodel.topads.TopadsShopUiModel

/**
 * @author by milhamj on 03/12/18.
 */
interface DynamicFeedTypeFactory {
    fun type(dynamicPostModel: DynamicPostModel): Int

    fun type(topadsShopUiModel: TopadsShopUiModel): Int

    fun type(topadsHeadlineUiModel: TopadsHeadlineUiModel): Int

    fun type(topadsHeadlineUiModel: TopadsHeadLineV2Model): Int

    fun type(topAdsBannerViewmodel: TopAdsBannerModel): Int

    fun type(carouselPlayCardModel: CarouselPlayCardModel): Int

    fun type(shopRecomWidgetModel: ShopRecomWidgetModel): Int

    fun type(shimmerUiModel: ShimmerUiModel): Int

    fun type(dynamicPostUiModel: DynamicPostUiModel) : Int
}
