package com.tokopedia.product.manage.feature.campaignstock.ui.customview.variantaccordion

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class VariantProductStockNameUiModel(
        val productName: String,
        val productStock: String): Visitable<VariantProductStockTypeFactory> {

    override fun type(typeFactory: VariantProductStockTypeFactory): Int =
            typeFactory.type(this)
}