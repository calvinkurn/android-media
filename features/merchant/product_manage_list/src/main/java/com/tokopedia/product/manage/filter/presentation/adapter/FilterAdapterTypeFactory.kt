package com.tokopedia.product.manage.filter.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.manage.filter.presentation.adapter.viewholder.SortViewHolder
import com.tokopedia.product.manage.filter.presentation.adapter.viewmodel.SortViewModel

class FilterAdapterTypeFactory: BaseAdapterTypeFactory(), FilterTypeFactory {

    override fun type(sortViewModel: SortViewModel): Int {
        return SortViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return super.createViewHolder(parent, type)
    }
}