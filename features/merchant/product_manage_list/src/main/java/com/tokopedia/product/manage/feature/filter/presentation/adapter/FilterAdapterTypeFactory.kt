package com.tokopedia.product.manage.feature.filter.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder.*
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.*

class FilterAdapterTypeFactory(
        private val itemClickListener: ItemClickListener,
        private val onExpandListener: OnExpandListener
): BaseAdapterTypeFactory(), FilterTypeFactory {

    override fun type(filterViewModel: FilterViewModel): Int {
        return FilterViewHolder.LAYOUT
    }

    override fun type(sortViewModel: SortViewModel): Int {
        return SortViewHolder.LAYOUT
    }

    override fun type(categoriesViewModel: CategoriesViewModel): Int {
        return CategoriesViewHolder.LAYOUT
    }

    override fun type(otherFilterViewModel: OtherFilterViewModel): Int {
        return OtherFilterViewHolder.LAYOUT
    }

    override fun type(etalaseViewModel: EtalaseViewModel): Int {
        return EtalaseViewHolder.LAYOUT
    }

    override fun type(headerViewModel: HeaderViewModel): Int {
        return HeaderViewHolder.LAYOUT
    }

    override fun type(seaAllViewModel: SeeAllViewModel): Int {
        return SeeAllViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            FilterViewHolder.LAYOUT -> FilterViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}