package com.tokopedia.product.manage.feature.list.view.adapter.factory

import com.tokopedia.product.manage.feature.list.view.model.ProductMenuViewModel

interface ProductMenuAdapterFactory {

    fun type(menuViewModel: ProductMenuViewModel): Int
}