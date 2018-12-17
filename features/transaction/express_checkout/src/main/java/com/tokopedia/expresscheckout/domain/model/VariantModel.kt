package com.tokopedia.expresscheckout.domain.model

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class VariantModel(
        var productVariantId: Int,
        var variantName: String,
        var identifier: String,
        var position: Int,
        var optionModels: ArrayList<OptionModel>
)