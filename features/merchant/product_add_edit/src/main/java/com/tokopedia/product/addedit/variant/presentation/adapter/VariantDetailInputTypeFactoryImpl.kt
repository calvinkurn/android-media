package com.tokopedia.product.addedit.variant.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.addedit.variant.presentation.adapter.uimodel.VariantDetailFieldsUiModel
import com.tokopedia.product.addedit.variant.presentation.adapter.uimodel.VariantDetailHeaderUiModel
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantDetailFieldsViewHolder
import com.tokopedia.product.addedit.variant.presentation.adapter.viewholder.VariantDetailHeaderViewHolder

class VariantDetailInputTypeFactoryImpl(
    private val onCollapsibleHeaderClickListener: VariantDetailHeaderViewHolder.OnCollapsibleHeaderClickListener,
    private val variantDetailFieldsViewHolderListener: VariantDetailFieldsViewHolder.VariantDetailFieldsViewHolderListener,
    private val enableVariantStatusChange: Boolean
) : BaseAdapterTypeFactory(), VariantDetailInputTypeFactory {

    override fun type(variantDetailHeaderUiModel: VariantDetailHeaderUiModel): Int {
        return VariantDetailHeaderViewHolder.LAYOUT
    }

    override fun type(variantDetailFieldsUiModel: VariantDetailFieldsUiModel): Int {
        return VariantDetailFieldsViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            VariantDetailHeaderViewHolder.LAYOUT -> VariantDetailHeaderViewHolder(
                parent,
                onCollapsibleHeaderClickListener
            )
            VariantDetailFieldsViewHolder.LAYOUT -> VariantDetailFieldsViewHolder(
                parent,
                variantDetailFieldsViewHolderListener,
                enableVariantStatusChange
            )
            else -> return super.createViewHolder(parent, type)
        }
    }
}
