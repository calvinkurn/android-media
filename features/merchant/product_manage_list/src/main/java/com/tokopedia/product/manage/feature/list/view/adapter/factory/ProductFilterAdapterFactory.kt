package com.tokopedia.product.manage.feature.list.view.adapter.factory

import com.tokopedia.product.manage.feature.list.view.model.FilterTabUiModel

interface ProductFilterAdapterFactory {

    fun type(uiModel: FilterTabUiModel): Int
}