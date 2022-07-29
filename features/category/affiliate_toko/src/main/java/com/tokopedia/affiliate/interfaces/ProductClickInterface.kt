package com.tokopedia.affiliate.interfaces

interface ProductClickInterface {
    fun onProductClick(productId : String, productName: String, productImage: String, productUrl: String, productIdentifier: String,status : Int?, type: String? = null)
}