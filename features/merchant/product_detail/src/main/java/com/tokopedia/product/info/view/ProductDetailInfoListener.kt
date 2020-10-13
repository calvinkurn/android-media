package com.tokopedia.product.info.view

/**
 * Created by Yehezkiel on 12/10/20
 */
interface ProductDetailInfoListener {
    fun onLoadingClick()
    fun closeAllExpand(uniqueIdentifier: Int, toggle:Boolean)
}