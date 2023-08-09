package com.tokopedia.product.detail.common

import android.content.Context
import android.content.Intent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace

object PostAtcHelper {

    /**
     * Additional Parameters for PostAtcActivity.kt
     */
    const val PARAM_LAYOUT_ID = "layoutID"
    const val PARAM_CART_ID = "cartID"
    const val PARAM_SELECTED_ADDONS_IDS = "selected_addons_ids"
    const val PARAM_DESELECTED_ADDONS_IDS = "deselected_addons_ids"
    const val PARAM_IS_FULFILLMENT = "is_fulfillment"
    const val PARAM_PAGE_SOURCE = "pageSource"
    const val PARAM_WAREHOUSE_ID = "warehouse_id"
    const val PARAM_QUANTITY = "quantity"

    fun start(
        context: Context,
        productId: String,
        cartId: String = "",
        isFulfillment: Boolean = false,
        layoutId: String = "",
        pageSource: Source = Source.Default,
        selectedAddonsIds: List<String> = emptyList(),
        deselectedAddonsIds: List<String> = emptyList(),
        warehouseId: String = "",
        quantity: Int
    ) {
        val intent = getIntent(
            context,
            productId,
            cartId,
            isFulfillment,
            layoutId,
            pageSource,
            selectedAddonsIds,
            deselectedAddonsIds,
            warehouseId,
            quantity
        )
        context.startActivity(intent)
    }

    fun getIntent(
        context: Context,
        productId: String,
        cartId: String = "",
        isFulfillment: Boolean = false,
        layoutId: String = "",
        pageSource: Source = Source.Default,
        selectedAddonsIds: List<String> = emptyList(),
        deselectedAddonsIds: List<String> = emptyList(),
        warehouseId: String = "",
        quantity: Int
    ): Intent {
        return RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.POST_ATC,
            productId
        ).apply {
            putExtra(PARAM_CART_ID, cartId)
            putExtra(PARAM_IS_FULFILLMENT, isFulfillment)
            putExtra(PARAM_LAYOUT_ID, layoutId)
            putExtra(PARAM_PAGE_SOURCE, pageSource.name)
            putExtra(PARAM_SELECTED_ADDONS_IDS, ArrayList(selectedAddonsIds))
            putExtra(PARAM_DESELECTED_ADDONS_IDS, ArrayList(deselectedAddonsIds))
            putExtra(PARAM_WAREHOUSE_ID, warehouseId)
            putExtra(PARAM_QUANTITY, quantity)
        }
    }

    sealed class Source(
        open val name: String
    ) {
        object PDP : Source("product detail page")
        object Default : Source("")
    }
}
