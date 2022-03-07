package com.tokopedia.feedcomponent.view.adapter.viewholder.topads

import com.tokopedia.topads.sdk.domain.model.CpmData
import com.tokopedia.topads.sdk.domain.model.CpmModel
import com.tokopedia.topads.sdk.domain.model.Product

interface TopAdsHeadlineListener {
    fun onFollowClick(positionInFeed: Int, shopId: String, adId: String)
    fun onTopAdsHeadlineImpression(position: Int, cpmModel: CpmModel, isNewVariant: Boolean = false)
    fun onTopAdsProductItemListsner(position: Int, product: Product, cpmData: CpmData)
    fun onTopAdsHeadlineAdsClick(position: Int, applink: String?, cpmData: CpmData, isNewVariant: Boolean = false)
    fun hideTopadsView(position: Int)
}