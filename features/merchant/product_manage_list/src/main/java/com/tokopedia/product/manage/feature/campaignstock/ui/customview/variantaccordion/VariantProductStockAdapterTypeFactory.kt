package com.tokopedia.product.manage.feature.campaignstock.ui.customview.variantaccordion

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

class VariantProductStockAdapterTypeFactory(private val onActionClicked: (Boolean) -> Unit): BaseAdapterTypeFactory(), VariantProductStockTypeFactory {

    override fun type(model: VariantProductStockActionUiModel): Int = VariantProductStockActionViewHolder.LAYOUT_RES

    override fun type(model: VariantProductStockNameUiModel): Int = VariantProductStockNameViewHolder.LAYOUT_RES

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>>? {
        return when(type) {
            VariantProductStockActionViewHolder.LAYOUT_RES -> VariantProductStockActionViewHolder(parent, onActionClicked)
            VariantProductStockNameViewHolder.LAYOUT_RES  -> VariantProductStockNameViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }


}