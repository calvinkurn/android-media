package com.tokopedia.expresscheckout.domain.model.atc

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class ShopShipmentModel(
        var shipId: Int? = 0,
        var shipName: String? = null,
        var shipCode: String? = null,
        var shipLogo: String? = null,
        var shipProdModels: ArrayList<ShipProdModel>? = null,
        var isDropshipEnabled: Int? = 0
)