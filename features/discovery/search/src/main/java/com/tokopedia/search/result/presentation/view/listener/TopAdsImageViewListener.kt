package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.SearchProductTopAdsImageDataView

interface TopAdsImageViewListener {

    fun onTopAdsImageViewImpressed(className: String?, searchTopAdsImageDataView: SearchProductTopAdsImageDataView)

    fun onTopAdsImageViewClick(searchTopAdsImageDataView: SearchProductTopAdsImageDataView)
}