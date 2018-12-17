package com.tokopedia.expresscheckout.domain.model

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class ProductVariantDataModel(
        var code: Int? = 0,
        var message: Int? = 0,
        var variantModels: ArrayList<VariantModel>,
        var childModels: ArrayList<ChildModel>,
        var isEnabled: Boolean,
        var stock: Int
)