package com.tokopedia.product.manage.feature.stockreminder.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.stockreminder.view.data.ProductStockReminderUiModel

class ProductStockReminderAdapterFactoryImpl(
    private val listener: ProductStockReminderViewHolder.ProductStockReminder
) : BaseAdapterTypeFactory(), ProductStockReminderAdapterFactory {


    override fun type(viewModel: ProductStockReminderUiModel): Int =
        ProductStockReminderViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            ProductStockReminderViewHolder.LAYOUT -> ProductStockReminderViewHolder(
                parent,
                listener
            )
            else -> return super.createViewHolder(parent, type)
        }
    }
}