package com.tokopedia.logisticcart.shipping.features.shippingcourierocc

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierAdapterListener
import com.tokopedia.logisticcart.shipping.features.shippingcourier.view.ShippingCourierViewHolder
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ArmyViewHolder
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.NotifierViewHolder
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.ShippingDurationAdapterListener
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel

class ShippingCourierOccAdapter(val list: List<RatesViewModelType>, private val isNewOccFlow: Boolean, private val shippingCourierAdapterListener: ShippingCourierAdapterListener, val shippingDurationAdapterListener: ShippingDurationAdapterListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val SHIPPING_COURIER_VIEW_HOLDER_TYPE = 5
        private const val SHIPPING_COURIER_VIEW_HOLDER_OCC_TYPE = 6
        private const val ARMY_VIEW_HOLDER_TYPE = 7
        private const val NOTIFIER_VIEW_HOLDER_TYPE = 8
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(getViewId(viewType), parent, false)
        return when (viewType) {
            SHIPPING_COURIER_VIEW_HOLDER_TYPE -> ShippingCourierViewHolder(view, 0)
            SHIPPING_COURIER_VIEW_HOLDER_OCC_TYPE -> ShippingCourierViewHolderOcc(view, 0)
            ARMY_VIEW_HOLDER_TYPE -> ArmyViewHolder(view)
            else -> NotifierViewHolder(view)
        }
    }

    private fun getViewId(viewType: Int): Int {
        return when (viewType) {
            SHIPPING_COURIER_VIEW_HOLDER_TYPE -> ShippingCourierViewHolder.ITEM_VIEW_SHIPMENT_COURIER
            SHIPPING_COURIER_VIEW_HOLDER_OCC_TYPE -> ShippingCourierViewHolderOcc.ITEM_VIEW_SHIPMENT_COURIER
            ARMY_VIEW_HOLDER_TYPE -> ArmyViewHolder.LAYOUT
            else -> NotifierViewHolder.LAYOUT
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShippingCourierViewHolder -> holder.bindData(list[position] as ShippingCourierUiModel, shippingCourierAdapterListener, position == itemCount - 1)
            is ShippingCourierViewHolderOcc -> holder.bindData(list[position] as ShippingCourierUiModel, shippingCourierAdapterListener, position == itemCount - 1)
            is ArmyViewHolder -> holder.bindData(list[position] as LogisticPromoUiModel, shippingDurationAdapterListener)
            is NotifierViewHolder -> holder.bindData()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (list[position]) {
            is ShippingCourierUiModel -> if (isNewOccFlow) SHIPPING_COURIER_VIEW_HOLDER_TYPE else SHIPPING_COURIER_VIEW_HOLDER_OCC_TYPE
            is LogisticPromoUiModel -> ARMY_VIEW_HOLDER_TYPE
            else -> NOTIFIER_VIEW_HOLDER_TYPE
        }
    }
}