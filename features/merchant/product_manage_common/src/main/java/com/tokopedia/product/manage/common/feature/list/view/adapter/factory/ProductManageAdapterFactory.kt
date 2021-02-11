package com.tokopedia.product.manage.common.feature.list.view.adapter.factory

import com.tokopedia.product.manage.common.feature.list.data.model.ProductUiModel

interface ProductManageAdapterFactory {
    fun type(uiModel: ProductUiModel): Int
}