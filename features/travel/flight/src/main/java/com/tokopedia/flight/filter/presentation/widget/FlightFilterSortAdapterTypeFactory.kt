package com.tokopedia.flight.filter.presentation.widget

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder

/**
 * @author by jessica on 2020-02-20
 */

class FlightFilterSortAdapterTypeFactory :
        BaseAdapterTypeFactory(), BaseListCheckableTypeFactory<Visitable<*>> {
    override fun type(viewModel: Visitable<*>): Int = FlightFilterSortViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return if (type == FlightFilterSortViewHolder.LAYOUT) TestViewHolder(parent) else
            super.createViewHolder(parent, type)
    }

}