package com.tokopedia.flight.promo_chips.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.databinding.ItemFlightPromoChipsBinding
import com.tokopedia.flight.promo_chips.presentation.adapter.viewholder.FlightPromoChipsViewHolder
import com.tokopedia.flight.promo_chips.data.model.AirlinePrice

/**
 * Created by astidhiyaa on 16/02/21.
 */

class FlightPromoChipsAdapterTypeFactory(private val onFlightPromoChipsListener: FlightPromoChipsViewHolder.OnFlightPromoChipsListener): BaseAdapterTypeFactory() {
    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            FlightPromoChipsViewHolder.LAYOUT -> {
                val binding = ItemFlightPromoChipsBinding.bind(parent)
                FlightPromoChipsViewHolder(binding, onFlightPromoChipsListener)
            }
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(lowestPrice: AirlinePrice): Int = FlightPromoChipsViewHolder.LAYOUT
}