package com.tokopedia.product.info.model.specification

import com.tokopedia.product.detail.R
import com.tokopedia.product.info.view.adapter.ProductSpecificationFactory

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