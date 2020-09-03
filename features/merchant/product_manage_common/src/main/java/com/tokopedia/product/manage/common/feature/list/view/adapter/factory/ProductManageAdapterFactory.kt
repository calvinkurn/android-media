package com.tokopedia.product.manage.common.feature.list.view.adapter.factory

import com.tokopedia.product.manage.common.feature.list.model.ProductViewModel

interface ProductManageAdapterFactory {
    fun type(viewModel: ProductViewModel): Int
}