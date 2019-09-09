package com.tokopedia.officialstore.presentation.adapter

import com.tokopedia.officialstore.presentation.adapter.viewmodel.BrandPopulerViewModel
import com.tokopedia.officialstore.presentation.adapter.viewmodel.CategoryViewModel
import com.tokopedia.officialstore.presentation.adapter.viewmodel.ExclusiveBrandViewModel
import com.tokopedia.officialstore.presentation.adapter.viewmodel.OfficialBannerViewModel

interface OfficialHomeTypeFactory {

    fun type(officialBannerViewModel: OfficialBannerViewModel): Int

    fun type(brandPopulerViewModel: BrandPopulerViewModel): Int

    fun type(categoryViewModel: CategoryViewModel): Int

    fun type(exclusiveBrandViewModel: ExclusiveBrandViewModel): Int
}