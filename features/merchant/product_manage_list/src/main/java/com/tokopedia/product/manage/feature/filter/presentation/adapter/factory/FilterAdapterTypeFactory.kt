package com.tokopedia.product.manage.feature.filter.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewholder.*
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.*
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChipsAdapter
import com.tokopedia.product.manage.feature.filter.presentation.widget.SeeAllListener
import com.tokopedia.product.manage.feature.filter.presentation.widget.ShowChipsListener

class FilterAdapterTypeFactory(
        private val seeAllListener: SeeAllListener,
        private val chipClickListener: ChipsAdapter.ChipClickListener,
        private val showChipsListener: ShowChipsListener
): BaseAdapterTypeFactory(), FilterTypeFactory {

    override fun type(filterUiModel: FilterUiModel): Int {
        return FilterViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            FilterViewHolder.LAYOUT -> FilterViewHolder(parent, seeAllListener, chipClickListener, showChipsListener)
            else -> super.createViewHolder(parent, type)
        }
    }
}