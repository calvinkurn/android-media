package com.tokopedia.addon.presentation.uimodel

import android.net.Uri
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.addon.presentation.uimodel.AddOnExtraConstant.CATEGORY_ID
import com.tokopedia.addon.presentation.uimodel.AddOnExtraConstant.CONDITION
import com.tokopedia.addon.presentation.uimodel.AddOnExtraConstant.DISCOUNTED_PRICE
import com.tokopedia.addon.presentation.uimodel.AddOnExtraConstant.PRICE
import com.tokopedia.addon.presentation.uimodel.AddOnExtraConstant.QUANTITY
import com.tokopedia.addon.presentation.uimodel.AddOnExtraConstant.SHOP_ID
import com.tokopedia.kotlin.extensions.view.toLongOrZero

object AddOnApplinkMapper {

    private const val SELECTED_ADDON_IDS = "selectedAddonIds"
    private const val DESELECTED_ADDON_IDS = "deselectedAddonIds"
    private const val SOURCE = "source"
    private const val CART_ID = "cartId"
    private const val WAREHOUSE_ID = "warehouseId"
    private const val IS_TOKOCABANG = "isTokocabang"
    private const val ATC_SOURCE = "atcSource"
    private const val PRODUCT_ID_SEGMENT_INDEX = 1
    private const val APPLINK_ARRAY_DELIMITER = ","

    fun getProductIdFromUri(uri: Uri): Long {
        return uri.pathSegments?.getOrNull(PRODUCT_ID_SEGMENT_INDEX).toLongOrZero()
    }

    fun getSelectedAddonIdsFromUri(uri: Uri): List<String> {
        var selectedProductIds: List<String> = emptyList()
        try {
            uri.getQueryParameter(SELECTED_ADDON_IDS)?.filterNot { it.isWhitespace() }?.let {
                if (it.isNotEmpty()) selectedProductIds = it.split(APPLINK_ARRAY_DELIMITER)
            }
        } catch (e: Exception) {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
        return selectedProductIds
    }

    fun getDeselectedAddonIdsFromUri(uri: Uri): List<String> {
        var selectedProductIds: List<String> = emptyList()
        try {
            uri.getQueryParameter(DESELECTED_ADDON_IDS)?.filterNot { it.isWhitespace() }?.let {
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

    fun getAtcSourceFromUri(uri: Uri): String {
        return uri.getQueryParameter(ATC_SOURCE).orEmpty()
    }

    fun getAddOnWidgetParamFromUri(uri: Uri): AddOnParam {
        val productId = getProductIdFromUri(uri).toString()
        val warehouseId = uri.getQueryParameter(WAREHOUSE_ID).orEmpty()
        val isTokocabang = uri.getQueryParameter(IS_TOKOCABANG).toBoolean()
        val categoryId = uri.getQueryParameter(CATEGORY_ID).toString()
        val shopId = uri.getQueryParameter(SHOP_ID).toString()
        val quantity = uri.getQueryParameter(QUANTITY).toLongOrZero()
        val price = uri.getQueryParameter(PRICE).toLongOrZero()
        val discountedPrice = uri.getQueryParameter(DISCOUNTED_PRICE).toLongOrZero()
        val condition = uri.getQueryParameter(CONDITION).toString()
        return AddOnParam(
            productId = productId,
            warehouseId = warehouseId,
            isTokocabang = isTokocabang,
            categoryID = categoryId,
            shopID = shopId,
            quantity = quantity,
            price = price,
            discountedPrice = discountedPrice,
            condition = condition,
        )
    }
}
