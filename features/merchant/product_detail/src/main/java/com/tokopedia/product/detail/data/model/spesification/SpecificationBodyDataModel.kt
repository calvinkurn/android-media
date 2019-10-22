package com.tokopedia.product.detail.data.model.spesification

import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.view.adapter.factory.ProductSpecificationFactory

data class SpecificationBodyDataModel(
        var title: String = "",
        var description: String = ""
) : ProductSpecificationDataModel {

    companion object {
        val LAYOUT = R.layout.item_specification_body
    }

    override fun type(typeFactory: ProductSpecificationFactory): Int {
        return typeFactory.type(this)
    }
}