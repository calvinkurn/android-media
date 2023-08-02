package com.tokopedia.product_bundle.common.data.mapper

import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.product_bundle.common.data.model.response.BundleInfo
import com.tokopedia.product_bundle.common.data.model.response.Child

object BundleDataMapper {

    private const val BUNDLE_INFO_ACTIVE: String = "1"
    private const val BUNDLE_ITEM_SHOW: String = "1"
    private const val BUNDLE_ITEM_ACTIVE: String = "ACTIVE"

    fun BundleInfo.isStockAvailable() = bundleItems.all {
        it.stock >= it.minOrder || it.children.isStockAvailable()
    }

    fun List<Child>.isStockAvailable() = any {
        it.stock >= it.minOrder
    }

    fun BundleInfo.isMinOrderValid() = bundleItems.all {
        it.minOrder.isMoreThanZero() || it.children.isMinOrderValid()
    }

    fun List<Child>.isMinOrderValid() = any {
        it.minOrder.isMoreThanZero()
    }

    fun List<BundleInfo>.getValidBundleInfo(): List<BundleInfo> {
        return filter {
            val bundleItem = it.bundleItems.firstOrNull()
            return@filter bundleItem != null &&
                it.status == BUNDLE_INFO_ACTIVE &&
                bundleItem.status == BUNDLE_ITEM_SHOW &&
                bundleItem.productStatus == BUNDLE_ITEM_ACTIVE &&
                it.isStockAvailable() &&
                it.isMinOrderValid()
        }
    }

}
