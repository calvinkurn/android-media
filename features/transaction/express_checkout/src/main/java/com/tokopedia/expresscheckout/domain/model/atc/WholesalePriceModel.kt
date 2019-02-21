package com.tokopedia.expresscheckout.domain.model.atc

/**
 * Created by Irfan Khoirul on 25/01/19.
 */

data class WholesalePriceModel(
        var qtyMinFmt: String? = "",
        var qtyMaxFmt: String? = "",
        var prdPrcFmt: String? = "",
        var qtyMin: Int = 0,
        var qtyMax: Int = 0,
        var prdPrc: Int = 0
)