package com.tokopedia.brandlist.brandlist_search.presentation.adapter

import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.*


interface BrandlistSearchTypeFactory {

    fun type(brandlistSearchResultUiModel: BrandlistSearchResultUiModel): Int

    fun type(brandlistSearchRecommendationUiModel: BrandlistSearchRecommendationUiModel): Int

    fun type(brandlistSearchNotFoundUiModel: BrandlistSearchNotFoundUiModel): Int

    fun type(brandlistSearchHeaderUiModel: BrandlistSearchHeaderUiModel): Int

    fun type(brandlistSearchShimmeringUiModel: BrandlistSearchShimmeringUiModel): Int

    fun type(brandlistSearchAllBrandGroupHeaderUiModel: BrandlistSearchAllBrandGroupHeaderUiModel): Int

    fun type(brandlistSearchRecommendationNotFoundUiModel: BrandlistSearchRecommendationNotFoundUiModel): Int

}