package com.tokopedia.officialstore.official.presentation.adapter.typefactory

import com.tokopedia.home_component.HomeComponentTypeFactory
import com.tokopedia.officialstore.base.diffutil.OfficialTypeFactory
import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.*
import com.tokopedia.officialstore.official.presentation.dynamic_channel.DynamicChannelViewModel

interface OfficialHomeTypeFactory : OfficialTypeFactory, HomeComponentTypeFactory {

    fun type(officialLoadingViewModel: OfficialLoadingViewModel): Int

    fun type(officialLoadingMoreViewModel: OfficialLoadingMoreViewModel): Int

    fun type(officialBannerViewModel: OfficialBannerViewModel): Int

    fun type(officialBenefitViewModel: OfficialBenefitViewModel): Int

    fun type(officialFeaturedShopViewModel: OfficialFeaturedShopViewModel): Int

    fun type(dynamicChannelViewModel: DynamicChannelViewModel): Int

    fun type(productRecommendationTitleViewModel: ProductRecommendationTitleViewModel): Int

    fun type(productRecommendationViewModel: ProductRecommendationViewModel): Int
}
