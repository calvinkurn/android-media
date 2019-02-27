package com.tokopedia.expresscheckout.domain.model.atc

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class ProductPreorderModel(
        var durationText: String? = null,
        var durationDay: Int = 0,
        var durationUnitCode: Int = 0,
        var durationUnitText: String? = null,
        var durationValue: String? = null
)