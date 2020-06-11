package com.tokopedia.home_component

import com.tokopedia.home_component.visitable.DynamicLegoBannerDataModel
import com.tokopedia.home_component.visitable.MixLeftDataModel
import com.tokopedia.home_component.visitable.MixTopDataModel
import com.tokopedia.home_component.visitable.RecommendationListCarouselDataModel

/**
 * Created by Devara on 2020-04-28
 */
interface HomeComponentTypeFactory {
    fun type(dynamicLegoBannerDataModel: DynamicLegoBannerDataModel): Int
    fun type(recommendationListCarouselDataModel: RecommendationListCarouselDataModel): Int
    fun type(mixLeftDataModel: MixLeftDataModel): Int
    fun type(mixTopDataModel: MixTopDataModel): Int
}