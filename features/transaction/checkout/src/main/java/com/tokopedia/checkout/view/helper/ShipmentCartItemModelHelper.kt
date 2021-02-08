package com.tokopedia.checkout.view.helper

import com.tokopedia.logisticcart.shipping.model.ShipmentCartItemModel

object ShipmentCartItemModelHelper {

    @JvmStatic
    fun getFirstProductId(models: List<ShipmentCartItemModel>): Long {
        models.firstOrNull()?.cartItemModels?.firstOrNull()?.let {
            return it.productId
        } ?: return 0
    }
}