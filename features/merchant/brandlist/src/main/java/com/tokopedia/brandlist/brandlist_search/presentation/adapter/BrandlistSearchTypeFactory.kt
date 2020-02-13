package com.tokopedia.brandlist.brandlist_search.presentation.adapter

import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchNotFoundViewModel
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchRecommendationTextViewModel
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchRecommendationViewModel
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchResultViewModel


interface BrandlistSearchTypeFactory {

    fun type(brandlistSearchResultViewModel: BrandlistSearchResultViewModel): Int

    fun type(brandlistSearchRecommendationViewModel: BrandlistSearchRecommendationViewModel): Int

    fun type(brandlistSearchNotFoundViewModel: BrandlistSearchNotFoundViewModel): Int

    fun type(brandlistSearchRecommendationTextViewModel: BrandlistSearchRecommendationTextViewModel): Int

}