package com.tokopedia.notifcenter.listener

import com.tokopedia.notifcenter.data.viewbean.ProductHighlightViewBean

interface ProductStockListener {
    fun productStockListCardImpression(data: ProductHighlightViewBean, index: Int)
    fun productStockListCardClicked(data: ProductHighlightViewBean)
    fun onAddToCartProduct(element: ProductHighlightViewBean)
}