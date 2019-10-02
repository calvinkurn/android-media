package com.tokopedia.officialstore.official.presentation.adapter

import com.tokopedia.officialstore.official.presentation.adapter.viewmodel.*

interface OfficialHomeTypeFactory {

    fun type(officialBannerViewModel: OfficialBannerViewModel): Int

    fun type(officialFeaturedShopViewModel: OfficialFeaturedShopViewModel): Int

    fun type(productRecommendationViewModel: ProductRecommendationViewModel): Int
}