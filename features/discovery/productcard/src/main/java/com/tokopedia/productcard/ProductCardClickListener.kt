package com.tokopedia.productcard

import android.view.View

interface ProductCardClickListener {
    fun onClick(v: View)
    fun onAreaClicked(v: View) {}
    fun onProductImageClicked(v: View) {}
    fun onSellerInfoClicked(v: View) {}
}
