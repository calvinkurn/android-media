package com.tokopedia.product.info.model.specification

import com.tokopedia.product.detail.R
import com.tokopedia.product.info.view.adapter.ProductSpecificationFactory

data class SpecificationTitleDataModel(
        var title: String = "") : ProductSpecificationDataModel {

    companion object{
        val LAYOUT = R.layout.item_specification_title
    }

    override fun type(typeFactory: ProductSpecificationFactory): Int = typeFactory.type(this)
}