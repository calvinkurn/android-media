package com.tokopedia.addon.presentation.uimodel

import android.net.Uri
import com.tokopedia.kotlin.extensions.view.toLongOrZero

object AddOnApplinkMapper {

    private const val SELECTED_ADDON_IDS = "selectedAddonIds"
    private const val SOURCE = "source"
    private const val CART_ID = "cartId"
    private const val PRODUCT_ID_SEGMENT_INDEX = 1
    private const val APPLINK_ARRAY_DELIMITER = ","

    fun getProductIdFromUri(uri: Uri): Long {
        return uri.pathSegments?.getOrNull(PRODUCT_ID_SEGMENT_INDEX).toLongOrZero()
    }

    fun getSelectedAddonIdsFromUri(uri: Uri): List<String> {
        var selectedProductIds: List<String> = emptyList()
        try {
            uri.getQueryParameter(SELECTED_ADDON_IDS)?.let {
                if (it.isNotEmpty()) selectedProductIds = it.split(APPLINK_ARRAY_DELIMITER)
            }
        } catch (e: Exception) { }
        return selectedProductIds
    }

    fun getPageSourceFromUri(uri: Uri): String {
        return uri.getQueryParameter(SOURCE).orEmpty()
    }

    fun getCartIdFromUri(uri: Uri): Long {
        return uri.getQueryParameter(CART_ID).toLongOrZero()
    }
}
