package com.tokopedia.notifcenter.listener

import com.tokopedia.notifcenter.data.viewbean.ProductHighlightViewBean

interface ProductStockListener {
    fun onAddToCartProduct(element: ProductHighlightViewBean)
}