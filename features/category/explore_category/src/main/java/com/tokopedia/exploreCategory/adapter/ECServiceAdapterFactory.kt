package com.tokopedia.exploreCategory.adapter

import android.view.View

import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.exploreCategory.ui.viewholder.ECShimmeringViewHolder
import com.tokopedia.exploreCategory.ui.viewholder.ECAccordionViewHolder
import com.tokopedia.exploreCategory.ui.viewholder.ECImageIconViewHolder
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.ECAccordionVHViewModel
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.ECImageIconVHViewModel
import com.tokopedia.exploreCategory.ui.viewholder.viewmodel.ECShimmerVHViewModel

class ECServiceAdapterFactory(private val accordionListener: ECAccordionViewHolder.AccordionListener?)
    : BaseAdapterTypeFactory(), ECServiceTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            ECShimmeringViewHolder.LAYOUT -> ECShimmeringViewHolder(parent)
            ECAccordionViewHolder.LAYOUT -> ECAccordionViewHolder(parent, accordionListener)
            ECImageIconViewHolder.LAYOUT -> ECImageIconViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }

    override fun type(viewModel: ECShimmerVHViewModel): Int {
        return ECShimmeringViewHolder.LAYOUT
    }

    override fun type(viewModel: ECAccordionVHViewModel): Int {
        return ECAccordionViewHolder.LAYOUT
    }

    override fun type(viewModel: ECImageIconVHViewModel): Int {
        return ECImageIconViewHolder.LAYOUT
    }
}
