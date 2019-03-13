package com.tokopedia.expresscheckout.domain.model.atc

import com.tokopedia.shipping_recommendation.domain.shipping.ShopShipment

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class GroupShopModel(
        var errors: ArrayList<String>? = null,
        var messages: ArrayList<String>? = null,
        var shopModel: ShopModel? = null,
        var shopShipmentModels: ArrayList<ShopShipment>? = null,
        var productModels: ArrayList<ProductModel>? = null,
        var shippingId: Int = 0,
        var spId: Int = 0,
        var dropshiperModel: DropshipperModel? = null,
        var isInsurance: Boolean? = false
)