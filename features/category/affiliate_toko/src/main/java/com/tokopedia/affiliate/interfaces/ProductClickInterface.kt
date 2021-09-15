package com.tokopedia.affiliate.interfaces

interface ProductClickInterface {
    fun onProductClick(productName: String, productImage: String, productUrl: String, productIdentifier: String,status : Int?)
}