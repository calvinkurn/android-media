package com.tokopedia.tokofood.feature.merchant.presentation.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokofood.feature.merchant.presentation.enums.ProductListItemType

data class ProductListItem(
        val listItemType: ProductListItemType,
        val productCategory: CategoryUiModel = CategoryUiModel(),
        val productUiModel: ProductUiModel = ProductUiModel()
): ImpressHolder()
