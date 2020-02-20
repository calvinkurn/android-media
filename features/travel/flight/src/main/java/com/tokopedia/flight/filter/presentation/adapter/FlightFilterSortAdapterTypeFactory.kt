package com.tokopedia.flight.filter.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.filter.presentation.adapter.viewholder.FlightFilterPriceRangeViewHolder
import com.tokopedia.flight.filter.presentation.model.PriceRangeModel

/**
 * @author by jessica on 2020-02-20
 */

class FlightFilterSortAdapterTypeFactory :
        BaseAdapterTypeFactory(){

    fun type(model: PriceRangeModel): Int = FlightFilterPriceRangeViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            FlightFilterPriceRangeViewHolder.LAYOUT ->
            else -> super.createViewHolder(parent, type)
        }
    }

}