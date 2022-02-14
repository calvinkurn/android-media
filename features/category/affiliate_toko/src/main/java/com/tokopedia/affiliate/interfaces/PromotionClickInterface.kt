package com.tokopedia.affiliate.interfaces

import com.tokopedia.affiliate.model.response.AffiliateSearchData

interface PromotionClickInterface {
    fun onPromotionClick(productId: String, productName: String, productImage: String, productUrl: String, productIdentifier: String)
    fun onButtonClick(errorCta  : AffiliateSearchData.SearchAffiliate.Data.Error.ErrorCta?)
}