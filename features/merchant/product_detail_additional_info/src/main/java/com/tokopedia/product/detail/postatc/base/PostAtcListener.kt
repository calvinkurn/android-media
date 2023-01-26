package com.tokopedia.product.detail.postatc.base

interface PostAtcListener {
    fun goToCart(cartId: String)
    fun goToAppLink(appLink: String)
    fun goToProduct(productId: String)
    fun refreshPage()
    fun fetchRecommendation(pageName: String)
}
