package com.tokopedia.brandlist.brandlist_page.presentation.adapter

import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.FeaturedBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.NewBrandViewModel
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.PopularBrandViewModel

interface BrandlistPageTypeFactory {

    fun type(featuredBrandViewModel: FeaturedBrandViewModel): Int

    fun type(popularBrandViewModel: PopularBrandViewModel): Int

    fun type(newBrandViewModel: NewBrandViewModel): Int

    fun type(allBrandViewModel: AllBrandViewModel): Int
}