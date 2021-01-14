package com.tokopedia.logisticcart.shipping.features.shippingcourier.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.NotifierModelSameDay
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

class ShippingCourierAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data: MutableList<RatesViewModelType> = mutableListOf()
    private var shippingCourierAdapterListener: ShippingCourierAdapterListener? = null
    private var cartPosition = 0

    fun setShippingCourierViewModels(shippingCourierUiModels: List<ShippingCourierUiModel>) {
        this.data = shippingCourierUiModels.toMutableList()
        if (shippingCourierUiModels[0].serviceData.serviceName == INSTAN_VIEW_TYPE) this.data.add(0, NotifierModel())
        if (shippingCourierUiModels[0].serviceData.serviceName == SAME_DAY_VIEW_TYPE) this.data.add(0, NotifierModelSameDay())
        notifyDataSetChanged()
    }

    fun setShippingCourierAdapterListener(shippingCourierAdapterListener: ShippingCourierAdapterListener?) {
        this.shippingCourierAdapterListener = shippingCourierAdapterListener
    }

    fun setCartPosition(cartPosition: Int) {
        this.cartPosition = cartPosition
    }

    override fun getItemViewType(position: Int): Int = when (data.get(position)) {
        is NotifierModel -> NotifierViewHolderInstant.LAYOUT
        is NotifierModelSameDay -> NotifierViewHolderSameDay.LAYOUT
        else -> ShippingCourierViewHolder.ITEM_VIEW_SHIPMENT_COURIER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            NotifierViewHolderInstant.LAYOUT -> NotifierViewHolderInstant(view)
            NotifierViewHolderSameDay.LAYOUT -> NotifierViewHolderSameDay(view)
            else -> ShippingCourierViewHolder(view, cartPosition)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShippingCourierViewHolder -> holder.bindData(data.get(position) as ShippingCourierUiModel, shippingCourierAdapterListener, position == itemCount -1)
            is NotifierViewHolderSameDay -> holder.bindData()
            is NotifierViewHolderInstant -> holder.bindData()
        }
    }

    companion object {
        private const val INSTAN_VIEW_TYPE = "Instan"
        private const val SAME_DAY_VIEW_TYPE = "Same Day"
    }

}