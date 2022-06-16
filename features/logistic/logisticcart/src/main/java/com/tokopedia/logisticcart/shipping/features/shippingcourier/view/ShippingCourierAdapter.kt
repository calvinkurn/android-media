package com.tokopedia.logisticcart.shipping.features.shippingcourier.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.data.constant.CourierConstant
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.PreOrderViewHolder
import com.tokopedia.logisticcart.shipping.model.*

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

class ShippingCourierAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data: MutableList<RatesViewModelType> = mutableListOf()
    private var shippingCourierAdapterListener: ShippingCourierAdapterListener? = null
    private var cartPosition = 0

    fun setShippingCourierViewModels(shippingCourierUiModels: List<ShippingCourierUiModel>, preOrderModel: PreOrderModel?, isOcc: Boolean) {
        this.data = shippingCourierUiModels.filter {courier -> !courier.productData.isUiRatesHidden}.toMutableList()
        if (preOrderModel?.display == true) {
            preOrderModel.let { this.data.add(0, it) }
            setNotifierModel(shippingCourierUiModels[0], 1, isOcc)
        } else {
            setNotifierModel(shippingCourierUiModels[0], 0, isOcc)
        }
        notifyDataSetChanged()
    }

    private fun setNotifierModel(shippingCourierUiModel: ShippingCourierUiModel, index: Int, isOcc: Boolean) {
        if (isOcc && shippingCourierUiModel.productData.shipperId in CourierConstant.INSTANT_SAMEDAY_COURIER) {
            this.data.add(index, NotifierModel())
        } else {
            if (shippingCourierUiModel.serviceData.serviceName == INSTAN_VIEW_TYPE) {
                this.data.add(index, NotifierModel())
            } else if (shippingCourierUiModel.serviceData.serviceName == SAME_DAY_VIEW_TYPE) {
                this.data.add(index, NotifierModelSameDay())
            }
        }
    }

    fun setShippingCourierAdapterListener(shippingCourierAdapterListener: ShippingCourierAdapterListener?) {
        this.shippingCourierAdapterListener = shippingCourierAdapterListener
    }

    fun setCartPosition(cartPosition: Int) {
        this.cartPosition = cartPosition
    }

    override fun getItemViewType(position: Int): Int = when (data[position]) {
        is PreOrderModel -> PreOrderViewHolder.LAYOUT
        is NotifierModel -> NotifierViewHolderInstant.LAYOUT
        is NotifierModelSameDay -> NotifierViewHolderSameDay.LAYOUT
        else -> ShippingCourierViewHolder.ITEM_VIEW_SHIPMENT_COURIER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            PreOrderViewHolder.LAYOUT -> PreOrderViewHolder(view)
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
            is PreOrderViewHolder -> holder.bindData(data[position] as PreOrderModel)
            is ShippingCourierViewHolder -> holder.bindData(data[position] as ShippingCourierUiModel, shippingCourierAdapterListener, position == itemCount -1)
            is NotifierViewHolderSameDay -> holder.bindData()
            is NotifierViewHolderInstant -> holder.bindData()
        }
    }

    companion object {
        private const val INSTAN_VIEW_TYPE = "Instan"
        private const val SAME_DAY_VIEW_TYPE = "Same Day"
    }

}