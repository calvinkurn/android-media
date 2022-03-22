package com.tokopedia.affiliate.interfaces

import com.tokopedia.affiliate.model.response.AffiliateSearchData

interface PromotionClickInterface {
    fun onPromotionClick(productId: String,shopId: String, productName: String, productImage: String, productUrl: String, productIdentifier: String, position: Int,commison:String)
    fun onButtonClick(errorCta  : AffiliateSearchData.SearchAffiliate.Data.Error.ErrorCta?)
}