package com.tokopedia.buyerorder.detail.analytics

import com.tokopedia.buyerorder.detail.data.OrderDetails
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailModel

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

    fun getCategoryName(orderDetails: RechargeOrderDetailModel?): String {
        orderDetails?.let {
            orderDetails.topSectionModel.titleData.find { it.label == CATEGORY_LABEL }?.let { data ->
                return data.detail
            }
            return ""
        }
        return ""
    }

    fun getProductName(orderDetails: RechargeOrderDetailModel?): String {
        orderDetails?.let {
            orderDetails.detailsSection.detailList.find { it.label == PRODUCT_LABEL }?.let {
                return it.detail
            }
            return ""
        }
        return ""
    }

    private const val CATEGORY_LABEL = "Kategori Produk"
    private const val PRODUCT_LABEL = "Jenis Layanan"
}