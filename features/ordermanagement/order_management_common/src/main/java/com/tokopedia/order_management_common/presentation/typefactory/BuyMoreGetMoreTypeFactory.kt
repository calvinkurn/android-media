package com.tokopedia.order_management_common.presentation.typefactory

import com.tokopedia.order_management_common.presentation.uimodel.ProductBmgmSectionUiModel

interface BuyMoreGetMoreTypeFactory {
    fun type(uiModel: ProductBmgmSectionUiModel): Int
}
