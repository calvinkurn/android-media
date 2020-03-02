package com.tokopedia.logisticcart.shipping.features.shippingcourierocc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierAdapterListener
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierViewHolder
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ArmyViewHolder
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.NotifierViewHolder
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationAdapterListener
import com.tokopedia.logisticcart.shipping.model.LogisticPromoViewModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierViewModel

class ShippingCourierOccAdapter(val list: List<RatesViewModelType>, val shippingCourierAdapterListener: ShippingCourierAdapterListener, val shippingDurationAdapterListener: ShippingDurationAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            ShippingCourierViewHolder.ITEM_VIEW_SHIPMENT_COURIER -> ShippingCourierViewHolder(view, 0)
            ArmyViewHolder.LAYOUT -> ArmyViewHolder(view)
            else -> NotifierViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShippingCourierViewHolder -> holder.bindData(list[position] as ShippingCourierViewModel, shippingCourierAdapterListener)
            is ArmyViewHolder -> holder.bindData(list[position] as LogisticPromoViewModel, shippingDurationAdapterListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is ShippingCourierViewModel -> ShippingCourierViewHolder.ITEM_VIEW_SHIPMENT_COURIER
            is LogisticPromoViewModel -> ArmyViewHolder.LAYOUT
            else -> NotifierViewHolder.LAYOUT
        }
    }
}