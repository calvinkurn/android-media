package com.tokopedia.logisticcart.shipping.features.shippingcourier.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticCommon.data.constant.CourierConstant
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.NotifierViewHolder
import com.tokopedia.logisticcart.shipping.features.shippingduration.view.PreOrderViewHolder
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.NotifierModel.Companion.TYPE_INSTAN
import com.tokopedia.logisticcart.shipping.model.NotifierModel.Companion.TYPE_DEFAULT
import com.tokopedia.logisticcart.shipping.model.NotifierModel.Companion.TYPE_SAMEDAY
import com.tokopedia.logisticcart.shipping.model.PreOrderModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

class ShippingCourierAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data: MutableList<RatesViewModelType> = mutableListOf()
    private var shippingCourierAdapterListener: ShippingCourierAdapterListener? = null
    private var cartPosition = 0
    private var isEndYearPromotion = false

    fun setShippingCourierViewModels(uiModel: MutableList<RatesViewModelType>) {
        this.data = uiModel
        notifyDataSetChanged()
    }

    fun setShippingCourierAdapterListener(shippingCourierAdapterListener: ShippingCourierAdapterListener?) {
        this.shippingCourierAdapterListener = shippingCourierAdapterListener
    }

    fun setCartPosition(cartPosition: Int) {
        this.cartPosition = cartPosition
    }

    fun setEndYearPromotion(endYearPromotion: Boolean) {
        this.isEndYearPromotion = endYearPromotion
    }

    override fun getItemViewType(position: Int): Int = when (data[position]) {
        is PreOrderModel -> PreOrderViewHolder.LAYOUT
        is NotifierModel -> NotifierViewHolder.LAYOUT
        else -> ShippingCourierViewHolder.ITEM_VIEW_SHIPMENT_COURIER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            PreOrderViewHolder.LAYOUT -> PreOrderViewHolder(view)
            NotifierViewHolder.LAYOUT -> NotifierViewHolder(view)
            else -> ShippingCourierViewHolder(view, cartPosition)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PreOrderViewHolder -> holder.bindData(data[position] as PreOrderModel)
            is ShippingCourierViewHolder -> holder.bindData(data[position] as ShippingCourierUiModel, shippingCourierAdapterListener, position == itemCount -1, isEndYearPromotion)
            is NotifierViewHolder -> holder.bindData(data[position] as NotifierModel)
        }
    }

}
