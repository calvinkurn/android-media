package com.tokopedia.brandlist.brandlist_page.presentation.adapter

import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.*

interface BrandlistPageTypeFactory {

    fun type(featuredBrandViewModel: FeaturedBrandViewModel): Int

    fun type(popularBrandViewModel: PopularBrandViewModel): Int

    fun type(newBrandViewModel: NewBrandViewModel): Int

    fun type(allBrandHeaderViewModel: AllBrandHeaderViewModel): Int

    fun type(allBrandGroupHeaderViewModel: AllBrandGroupHeaderViewModel): Int

    fun type(allBrandViewModel: AllBrandViewModel): Int

    fun type(allbrandNotFoundViewModel: AllbrandNotFoundViewModel): Int
}