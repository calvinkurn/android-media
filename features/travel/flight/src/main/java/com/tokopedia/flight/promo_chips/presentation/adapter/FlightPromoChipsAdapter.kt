package com.tokopedia.flight.promo_chips.presentation.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.promo_chips.presentation.adapter.viewholder.FlightPromoChipsViewHolder

/**
 * Created by astidhiyaa on 16/02/21.
 */

class FlightPromoChipsAdapter(val context: Context,
                              flightPromoChipsAdapterTypeFactory: FlightPromoChipsAdapterTypeFactory)
    : BaseListAdapter<Visitable<*>, FlightPromoChipsAdapterTypeFactory>(flightPromoChipsAdapterTypeFactory) {

    var selectedPosition = SELECTED_POSITION_INIT

    override fun onBindViewHolder(holder: AbstractViewHolder<out Visitable<*>>, position: Int, payloads: MutableList<Any>) {
        if (holder is FlightPromoChipsViewHolder) {
            holder.adapter = this
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    fun renderList(data: List<Visitable<*>>) {
        clearAllElements()
        addElement(data)
    }

    fun setSelectedProduct(position: Int) {
        if (selectedPosition > -1) {
            val oldPosition = selectedPosition
            notifyItemChanged(oldPosition)
        }
        selectedPosition = position
        notifyItemChanged(position)
    }

    companion object{
        const val SELECTED_POSITION_INIT = -1
    }
}
