package com.tokopedia.officialstore.presentation.adapter

import com.tokopedia.officialstore.presentation.adapter.viewmodel.*

interface OfficialHomeTypeFactory {

    fun type(officialBannerViewModel: OfficialBannerViewModel): Int

    fun type(brandPopulerViewModel: BrandPopulerViewModel): Int

    fun type(categoryViewModel: CategoryViewModel): Int

    fun type(exclusiveBrandViewModel: ExclusiveBrandViewModel): Int

    fun type(productRecommendationViewModel: ProductRecommendationViewModel): Int
}