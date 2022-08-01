package com.tokopedia.affiliate.interfaces

import com.tokopedia.affiliate.model.response.AffiliatePerformanceListData
import com.tokopedia.affiliate.model.response.AffiliateSearchData

interface AffiliateItemImpressionListener

interface AffiliatePromoImpressionListener : AffiliateItemImpressionListener {
    fun onItemImpression(
        item: AffiliateSearchData.SearchAffiliate.Data.Card.Item,
        position: Int,
        type: String
    )
}

interface AffiliateHomeImpressionListener : AffiliateItemImpressionListener{
    fun onItemImpression(
        item: AffiliatePerformanceListData.GetAffiliatePerformanceList.Data.Data.Item,
        position: Int,
        type: String
    )
}