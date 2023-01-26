package com.tokopedia.product.detail.postatc.base

interface PostAtcListener {
    fun goToCart(cartId: String)
    fun goToAppLink(applink: String)
    fun goToProduct(productId: String)
}
