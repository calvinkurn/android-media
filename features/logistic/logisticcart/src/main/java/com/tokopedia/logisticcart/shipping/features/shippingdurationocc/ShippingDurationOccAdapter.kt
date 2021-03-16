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

class ShippingDurationOccAdapter(private val isOccNewFlow: Boolean, private val list: List<RatesViewModelType>, private val shippingDurationAdapterListener: ShippingDurationAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val SHIPPING_DURATION_VIEW_HOLDER_TYPE = 1
        private const val SHIPPING_DURATION_VIEW_HOLDER_OCC_TYPE = 2
        private const val ARMY_VIEW_HOLDER_TYPE = 3
        private const val NOTIFIER_VIEW_HOLDER_TYPE = 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(getViewId(viewType), parent, false)
        return when (viewType) {
            SHIPPING_DURATION_VIEW_HOLDER_OCC_TYPE -> ShippingDurationViewHolderOcc(view, 0)
            SHIPPING_DURATION_VIEW_HOLDER_TYPE -> ShippingDurationViewHolder(view, 0)
            ARMY_VIEW_HOLDER_TYPE -> ArmyViewHolder(view)
            else -> NotifierViewHolder(view)
        }
    }

    private fun getViewId(viewType: Int): Int {
        return when (viewType) {
            SHIPPING_DURATION_VIEW_HOLDER_OCC_TYPE -> ShippingDurationViewHolderOcc.ITEM_VIEW_SHIPMENT_DURATION
            SHIPPING_DURATION_VIEW_HOLDER_TYPE -> ShippingDurationViewHolder.ITEM_VIEW_SHIPMENT_DURATION
            ARMY_VIEW_HOLDER_TYPE -> ArmyViewHolder.LAYOUT
            else -> NotifierViewHolder.LAYOUT
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShippingDurationViewHolderOcc -> holder.bindData(list[position] as ShippingDurationUiModel, shippingDurationAdapterListener, true)
            is ShippingDurationViewHolder -> holder.bindData(list[position] as ShippingDurationUiModel, shippingDurationAdapterListener, true)
            is ArmyViewHolder -> holder.bindData(list[position] as LogisticPromoUiModel, shippingDurationAdapterListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is ShippingDurationUiModel -> if (isOccNewFlow) SHIPPING_DURATION_VIEW_HOLDER_TYPE else SHIPPING_DURATION_VIEW_HOLDER_OCC_TYPE
            is LogisticPromoUiModel -> ARMY_VIEW_HOLDER_TYPE
            else -> NOTIFIER_VIEW_HOLDER_TYPE
        }
    }
}