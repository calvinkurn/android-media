package com.tokopedia.affiliate.interfaces

interface PromotionClickInterface {
    fun onPromotionClick(productName: String, productImage: String, productUrl: String, productIdentifier: String)
    fun onViewMoreClick()
    fun onChangeLinkClick()
}