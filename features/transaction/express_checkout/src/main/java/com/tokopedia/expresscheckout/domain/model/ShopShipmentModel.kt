package com.tokopedia.expresscheckout.domain.model

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class ShopShipmentModel(
        var shipId: Int = 0,
        var shipName: String? = null,
        var shipCode: String? = null,
        var shipLogo: String? = null,
        var shipProdModels: ArrayList<ShipProdModel>,
        var isDropshipEnabled: Int = 0
)