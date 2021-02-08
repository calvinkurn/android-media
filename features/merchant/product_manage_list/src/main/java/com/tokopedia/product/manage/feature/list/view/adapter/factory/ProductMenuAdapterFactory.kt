package com.tokopedia.product.manage.feature.list.view.adapter.factory

import com.tokopedia.product.manage.feature.list.view.model.ProductItemDivider
import com.tokopedia.product.manage.feature.list.view.model.ProductMenuUiModel

interface ProductMenuAdapterFactory {

    fun type(item: ProductMenuUiModel): Int
    fun type(item: ProductItemDivider): Int
}