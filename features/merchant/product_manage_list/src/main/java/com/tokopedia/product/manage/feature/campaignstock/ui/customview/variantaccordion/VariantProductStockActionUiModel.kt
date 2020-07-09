package com.tokopedia.product.manage.feature.campaignstock.ui.customview.variantaccordion

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class VariantProductStockActionUiModel(val actionWording: String,
                                            var isAccordionOpened: Boolean = false): Visitable<VariantProductStockTypeFactory> {

    override fun type(typeFactory: VariantProductStockTypeFactory): Int =
            typeFactory.type(this)
}