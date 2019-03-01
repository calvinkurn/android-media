package com.tokopedia.expresscheckout.domain.model.atc

/**
 * Created by Irfan Khoirul on 17/12/18.
 */

data class ProductVariantDataModel(
        var parentId: Int? = 0,
        var defaultChild: Int? = 0,
        var variantModels: ArrayList<VariantModel>? = null,
        var childModels: ArrayList<ChildModel>? = null,
        var isEnabled: Boolean? = false,
        var stock: Int? = 0
)