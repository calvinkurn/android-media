package com.tokopedia.oldproductbundle.common.data.mapper

import android.net.Uri
import com.tokopedia.kotlin.extensions.view.toLongOrZero

// Applink sample = tokopedia-android-internal/product-bundle/2147881200/?bundleId=3&selectedProductIds=12,45,67&source=cart&cartIds=1,2,3
object ProductBundleApplinkMapper {

    private const val BUNDLE_ID = "bundleId"
    private const val SELECTED_PRODUCT_IDS = "selectedProductIds"
    private const val SOURCE = "source"
    private const val WAREHOUSE_ID = "warehouseId"
    private const val DEFAULT_VALUE_APPLINK_DATA = 0L
    private const val PRODUCT_ID_SEGMENT_INDEX = 1
    private const val PRODUCT_ID_SEGMENT_SIZE_MIN = 2
    private const val APPLINK_ARRAY_DELIMITER = ","

    const val DEFAULT_VALUE_WAREHOUSE_ID = "0"

    fun getProductIdFromUri(uri: Uri, pathSegments: List<String>): Long {
        return if (pathSegments.size >= PRODUCT_ID_SEGMENT_SIZE_MIN) {
            uri.pathSegments?.getOrNull(PRODUCT_ID_SEGMENT_INDEX).toLongOrZero()
        } else {
            DEFAULT_VALUE_APPLINK_DATA
        }
    }

    fun getBundleIdFromUri(uri: Uri): Long {
        return try {
            if (uri.getQueryParameter(BUNDLE_ID) == null) DEFAULT_VALUE_APPLINK_DATA
            else uri.getQueryParameter(BUNDLE_ID).toLongOrZero()
        } catch (e: Exception) {
            DEFAULT_VALUE_APPLINK_DATA
        }
    }

    fun getSelectedProductIdsFromUri(uri: Uri): List<String> {
        var selectedProductIds: List<String> = emptyList()
        try {
            uri.getQueryParameter(SELECTED_PRODUCT_IDS)?.let {
                if (it.isNotEmpty()) selectedProductIds = it.split(APPLINK_ARRAY_DELIMITER)
            }
        } catch (e: Exception) { }
        return selectedProductIds
    }

    fun getPageSourceFromUri(uri: Uri): String {
        return uri.getQueryParameter(SOURCE).orEmpty()
    }

    fun getWarehouseIdFromUri(uri: Uri): String {
        return uri.getQueryParameter(WAREHOUSE_ID) ?: DEFAULT_VALUE_WAREHOUSE_ID
    }

}