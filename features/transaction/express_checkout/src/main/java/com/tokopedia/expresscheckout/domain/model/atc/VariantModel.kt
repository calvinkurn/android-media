package com.tokopedia.expresscheckout.domain.model.atc

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class VariantModel(
        var productVariantId: Int? = 0,
        var variantName: String? = null,
        var identifier: String? = null,
        var position: Int? = 0,
        var optionModels: ArrayList<OptionModel>? = null
)