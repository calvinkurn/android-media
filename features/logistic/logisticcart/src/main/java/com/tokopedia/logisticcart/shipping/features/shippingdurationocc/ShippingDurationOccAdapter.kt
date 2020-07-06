package com.tokopedia.logisticcart.shipping.features.shippingdurationocc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ArmyViewHolder
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.NotifierViewHolder
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationAdapterListener
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationViewHolder
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel

class ShippingDurationOccAdapter(private val list: List<RatesViewModelType>, private val shippingDurationAdapterListener: ShippingDurationAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            ShippingDurationViewHolder.ITEM_VIEW_SHIPMENT_DURATION -> ShippingDurationViewHolder(view, 0)
            ArmyViewHolder.LAYOUT -> ArmyViewHolder(view)
            else -> NotifierViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShippingDurationViewHolder -> holder.bindData(list[position] as ShippingDurationUiModel, shippingDurationAdapterListener, true)
            is ArmyViewHolder -> holder.bindData(list[position] as LogisticPromoUiModel, shippingDurationAdapterListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is ShippingDurationUiModel -> ShippingDurationViewHolder.ITEM_VIEW_SHIPMENT_DURATION
            is LogisticPromoUiModel -> ArmyViewHolder.LAYOUT
            else -> NotifierViewHolder.LAYOUT
        }
    }
}