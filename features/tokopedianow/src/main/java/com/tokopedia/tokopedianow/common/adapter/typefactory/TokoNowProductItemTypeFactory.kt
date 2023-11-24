package com.tokopedia.tokopedianow.common.adapter.typefactory

import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView

interface TokoNowProductItemTypeFactory {
    fun type(productItemDataView: ProductItemDataView): Int
}
