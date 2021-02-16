package com.tokopedia.flight.promo_chips.adapter

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.promo_chips.adapter.viewholder.FlightPromoChipsViewHolder

/**
 * Created by astidhiyaa on 16/02/21.
 */

class FlightPromoChipsAdapter(val context: Context,
                              flightPromoChipsAdapterTypeFactory: FlightPromoChipsAdapterTypeFactory)
    : BaseListAdapter<Visitable<*>, FlightPromoChipsAdapterTypeFactory>(flightPromoChipsAdapterTypeFactory) {

    var selectedPosition = START_POSITION

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

    fun clearList(){
        clearAllElements()
    }

    fun selectPromo(position: Int) {
        selectedPosition = position
    }

    companion object{
        const val START_POSITION = 0
    }
}
