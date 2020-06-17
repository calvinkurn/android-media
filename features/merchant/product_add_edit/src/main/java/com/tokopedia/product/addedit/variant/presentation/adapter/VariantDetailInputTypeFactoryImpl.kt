package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantDetailFieldsViewHolder
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantDetailHeaderViewHolder
import com.tokopedia.product.addedit.variant.presentation.adapter.viewmodel.VariantDetailFieldsViewModel
import com.tokopedia.product.addedit.variant.presentation.adapter.viewmodel.VariantDetailHeaderViewModel

class VariantDetailInputTypeFactoryImpl(val listener: VariantDetailHeaderViewHolder.OnCollapsibleHeaderClickListener) :
        BaseAdapterTypeFactory(), VariantDetailInputTypeFactory {

    override fun type(variantDetailHeaderViewModel: VariantDetailHeaderViewModel): Int {
        return VariantDetailHeaderViewHolder.LAYOUT
    }

    override fun type(variantDetailFieldsViewModel: VariantDetailFieldsViewModel): Int {
        return VariantDetailFieldsViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            VariantDetailHeaderViewHolder.LAYOUT -> VariantDetailHeaderViewHolder(parent, listener)
            VariantDetailFieldsViewHolder.LAYOUT -> VariantDetailFieldsViewHolder(parent)
            else -> return super.createViewHolder(parent, type)
        }
    }
}