package com.tokopedia.officialstore.official.presentation.adapter

import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.*

interface OfficialHomeTypeFactory {

    fun type(officialBannerViewModel: OfficialBannerViewModel): Int

    fun type(officialBenefitViewModel: OfficialBenefitViewModel): Int

    fun type(officialFeaturedShopViewModel: OfficialFeaturedShopViewModel): Int

    fun type(dynamicChannelViewModel: DynamicChannelViewModel): Int

    fun type(productRecommendationTitleViewModel: ProductRecommendationTitleViewModel): Int

    fun type(productRecommendationViewModel: ProductRecommendationViewModel): Int
}
