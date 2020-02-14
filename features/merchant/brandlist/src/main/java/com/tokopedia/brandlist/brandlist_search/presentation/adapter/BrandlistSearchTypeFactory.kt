package com.tokopedia.brandlist.brandlist_search.presentation.adapter

import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.*


interface BrandlistSearchTypeFactory {

    fun type(brandlistSearchResultViewModel: BrandlistSearchResultViewModel): Int

    fun type(brandlistSearchRecommendationViewModel: BrandlistSearchRecommendationViewModel): Int

    fun type(brandlistSearchNotFoundViewModel: BrandlistSearchNotFoundViewModel): Int

    fun type(brandlistSearchRecommendationTextViewModel: BrandlistSearchRecommendationTextViewModel): Int

    fun type(brandlistSearchShimmeringViewModel: BrandlistSearchShimmeringViewModel): Int

}