package com.tokopedia.product.manage.feature.filter.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder.*
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.*
import com.tokopedia.product.manage.feature.filter.presentation.widget.SeeAllListener

class FilterAdapterTypeFactory(
        private val seeAllListener: SeeAllListener
): BaseAdapterTypeFactory(), FilterTypeFactory {

    override fun type(filterViewModel: FilterViewModel): Int {
        return FilterViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            FilterViewHolder.LAYOUT -> FilterViewHolder(parent, seeAllListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}