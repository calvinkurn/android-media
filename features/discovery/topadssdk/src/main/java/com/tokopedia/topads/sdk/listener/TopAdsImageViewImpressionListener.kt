package com.tokopedia.topads.sdk.listener

import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

interface TopAdsImageViewImpressionListener {
    fun onTopAdsImageViewImpression( topAdsModel : TopAdsImageViewModel )
}
