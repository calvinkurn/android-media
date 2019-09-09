package com.tokopedia.officialstore.presentation.adapter

import com.tokopedia.officialstore.presentation.adapter.viewmodel.BrandPopulerViewModel
import com.tokopedia.officialstore.presentation.adapter.viewmodel.CategoryViewModel
import com.tokopedia.officialstore.presentation.adapter.viewmodel.ExclusiveBrandViewModel

interface OfficialHomeTypeFactory {

    fun type(brandPopulerViewModel: BrandPopulerViewModel): Int

    fun type(categoryViewModel: CategoryViewModel): Int

    fun type(exclusiveBrandViewModel: ExclusiveBrandViewModel): Int
}