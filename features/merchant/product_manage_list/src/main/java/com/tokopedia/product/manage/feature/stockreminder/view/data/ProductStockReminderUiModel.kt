package com.tokopedia.product.manage.feature.stockreminder.view.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.product.manage.common.feature.variant.adapter.model.ProductVariant
import com.tokopedia.product.manage.common.feature.variant.data.model.Picture
import com.tokopedia.product.manage.common.feature.variant.data.model.Product
import com.tokopedia.product.manage.common.feature.variant.data.model.Selection
import com.tokopedia.product.manage.feature.stockreminder.view.adapter.ProductStockReminderAdapterFactory

data class ProductStockReminderUiModel(
    val id: String,
    val productName: String,
    val stockAlertCount: Int,
    val stockAlertStatus: Int,
    val stock:Int
) : Visitable<ProductStockReminderAdapterFactory> {

    override fun type(typeFactory: ProductStockReminderAdapterFactory): Int {
        return typeFactory.type(this)
    }

}