package com.tokopedia.topads.sdk.listener

import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.Product

abstract class TopAdsItemImpressionListener {
    open fun onImpressionProductAdsItem(position: Int, product: Product?, data: CpmData) {}
    open fun onImpressionHeadlineAdsItem(position: Int, data: CpmData) {}
}
