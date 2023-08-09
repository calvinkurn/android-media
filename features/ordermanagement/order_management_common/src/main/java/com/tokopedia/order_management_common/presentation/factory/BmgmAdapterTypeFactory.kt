package com.tokopedia.order_management_common.presentation.factory

import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel

interface BmgmAdapterTypeFactory {
    fun type(productBmgmSectionUiModel: ProductBmgmSectionUiModel): Int
}
