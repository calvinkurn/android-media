package com.tokopedia.flight.cancellationdetail.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.cancellationdetail.presentation.adapter.viewholder.FlightOrderCancellationDetailRouteViewHolder
import com.tokopedia.flight.orderdetail.presentation.model.FlightOrderDetailRouteModel

/**
 * @author by furqan on 07/01/2021
 */
class FlightOrderCancellationDetailRouteAdapterTypeFactory(
        private val listener: FlightOrderCancellationDetailRouteViewHolder.Listener,
        private val isShowRefundableTag: Boolean)
    : BaseAdapterTypeFactory(), FlightOrderCancellationDetailRouteTypeFactory {

    override fun type(model: FlightOrderDetailRouteModel): Int =
            FlightOrderCancellationDetailRouteViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> =
            when (type) {
                FlightOrderCancellationDetailRouteViewHolder.LAYOUT -> FlightOrderCancellationDetailRouteViewHolder(listener, isShowRefundableTag, parent)
                else -> super.createViewHolder(parent, type)
            }
}