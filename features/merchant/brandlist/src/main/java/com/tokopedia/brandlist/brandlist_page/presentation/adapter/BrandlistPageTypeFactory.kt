package com.tokopedia.brandlist.brandlist_page.presentation.adapter

import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.*

interface BrandlistPageTypeFactory {

    fun type(featuredBrandUiModel: FeaturedBrandUiModel): Int

    fun type(popularBrandUiModel: PopularBrandUiModel): Int

    fun type(newBrandUiModel: NewBrandUiModel): Int

    fun type(allBrandHeaderUiModel: AllBrandHeaderUiModel): Int

    fun type(allBrandGroupHeaderUiModel: AllBrandGroupHeaderUiModel): Int

    fun type(allBrandUiModel: AllBrandUiModel): Int

    fun type(allbrandNotFoundUiModel: AllbrandNotFoundUiModel): Int
}