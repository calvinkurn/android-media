package com.tokopedia.product.manage.feature.campaignstock.ui.customview.variantaccordion

interface VariantProductStockTypeFactory {

    fun type(model: VariantProductStockActionUiModel): Int
    fun type(model: VariantProductStockNameUiModel): Int

}