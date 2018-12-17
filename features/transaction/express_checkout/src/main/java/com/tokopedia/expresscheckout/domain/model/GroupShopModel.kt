package com.tokopedia.expresscheckout.domain.model

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class GroupShopModel(
        var errors: ArrayList<String>? = null,
        var messages: ArrayList<String>? = null,
        var shopModel: ShopModel? = null,
        var shopShipmentModels: ArrayList<ShopShipmentModel>? = null,
        var productModels: ArrayList<ProductModel>? = null,
        var shippingId: Int = 0,
        var spId: Int = 0,
        var dropshiper: DropshipperModel? = null,
        var isInsurance: Boolean? = false
)