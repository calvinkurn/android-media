package com.tokopedia.buyerorder.detail.analytics

import com.tokopedia.buyerorder.detail.data.OrderDetails

object OrderListAnalyticsUtils {
    fun getCategoryName(orderDetails: OrderDetails): String {
        orderDetails.title().find { it.label() == CATEGORY_LABEL }?.run {
            return value()
        }
        return ""
    }

    fun getProductName(orderDetails: OrderDetails): String {
        orderDetails.detail().find { it.label() == PRODUCT_LABEL }?.run {
            return value()
        }
        return ""
    }

    private const val CATEGORY_LABEL = "Kategori Produk"
    private const val PRODUCT_LABEL = "Jenis Layanan"
}