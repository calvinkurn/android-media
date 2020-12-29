package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageViewModel

interface TopAdsImageViewListener {

    fun onTopAdsImageViewImpressed(className: String?, searchTopAdsImageViewModel: SearchProductTopAdsImageViewModel)

    fun onTopAdsImageViewClick(searchTopAdsImageViewModel: SearchProductTopAdsImageViewModel)
}