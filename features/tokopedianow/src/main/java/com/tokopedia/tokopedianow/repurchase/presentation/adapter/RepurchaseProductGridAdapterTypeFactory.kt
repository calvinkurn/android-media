package com.tokopedia.tokopedianow.repurchase.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.repurchase.presentation.uimodel.RepurchaseProductUiModel
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseProductViewHolder
import com.tokopedia.tokopedianow.repurchase.presentation.viewholder.RepurchaseProductViewHolder.*

class RepurchaseProductGridAdapterTypeFactory(
    private val productCardListener: RepurchaseProductCardListener
): BaseAdapterTypeFactory(), RepurchaseProductGridTypeFactory {
    override fun type(uiModel: RepurchaseProductUiModel): Int {
        return RepurchaseProductViewHolder.LAYOUT
    }

    override fun createViewHolder(
        view: View,
        type: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            RepurchaseProductViewHolder.LAYOUT -> {
                RepurchaseProductViewHolder(view, productCardListener)
            }
            else -> super.createViewHolder(view, type)
        }
    }
}
