package com.tokopedia.expresscheckout.domain.model.atc

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class ShipProdModel(
        var shipProdId: Int = 0,
        var shipProdName: String? = null,
        var shipGroupName: String? = null,
        var shipGroupId: Int = 0,
        var additionalFee: Int = 0,
        var minimumWeight: Int = 0
)