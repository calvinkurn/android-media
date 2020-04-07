package com.tokopedia.product.manage.feature.list.view.adapter.factory

import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel

interface ProductManageAdapterFactory {
    fun type(viewModel: ProductViewModel): Int
}