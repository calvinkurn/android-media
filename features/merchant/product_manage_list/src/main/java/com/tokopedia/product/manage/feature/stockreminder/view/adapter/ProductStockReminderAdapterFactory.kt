package com.tokopedia.product.manage.feature.stockreminder.view.adapter

import com.tokopedia.product.manage.feature.stockreminder.view.data.ProductStockReminderUiModel

interface ProductStockReminderAdapterFactory {

    fun type(viewModel: ProductStockReminderUiModel): Int
}