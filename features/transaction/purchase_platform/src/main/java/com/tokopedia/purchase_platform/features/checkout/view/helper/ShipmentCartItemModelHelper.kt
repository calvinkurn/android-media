package com.tokopedia.purchase_platform.features.checkout.view.helper

import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel

object ShipmentCartItemModelHelper {

    @JvmStatic
    fun getFirstProductId(models: List<ShipmentCartItemModel>): String? {
        models.firstOrNull()?.cartItemModels?.firstOrNull()?.let {
            return it.productId.toString()
        } ?: return null
    }
}