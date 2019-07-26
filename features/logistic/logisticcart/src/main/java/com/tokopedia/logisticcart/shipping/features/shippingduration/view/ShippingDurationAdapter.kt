package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.logisticcart.shipping.model.LogisticPromoViewModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType

import com.tokopedia.logisticcart.shipping.model.ShippingDurationViewModel

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

class ShippingDurationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mData: MutableList<RatesViewModelType>
    private var shippingDurationAdapterListener: ShippingDurationAdapterListener? = null
    private var cartPosition: Int = 0

    init {
        mData = mutableListOf()
    }

    fun setShippingDurationViewModels(shippingDurationViewModels: List<ShippingDurationViewModel>, promoViewModel: LogisticPromoViewModel?) {
        this.mData = shippingDurationViewModels.toMutableList()
        promoViewModel?.let { this.mData.add(0, it) }
        notifyDataSetChanged()
    }

    fun setShippingDurationAdapterListener(shippingDurationAdapterListener: ShippingDurationAdapterListener) {
        this.shippingDurationAdapterListener = shippingDurationAdapterListener
    }

    fun setCartPosition(cartPosition: Int) {
        this.cartPosition = cartPosition
    }

    fun getRatesDataFromLogisticPromo(serId: Int): ShippingDurationViewModel? {
        mData.firstOrNull { it is ShippingDurationViewModel && it.serviceData.serviceId == serId }
                ?.let {
                    if (it is ShippingDurationViewModel) {
                        return it
                    }
                }
        return null
    }

    fun initiateShowcase() {
        var position = 0
        for (mDatum in mData) {
            if (mDatum is ShippingDurationViewModel) {
                mDatum.isShowShowCase = true
                break
            }
            position++
        }
        notifyItemChanged(position)
    }

    override fun getItemViewType(position: Int): Int = when (mData.get(position)) {
        is LogisticPromoViewModel -> LogisticPromoViewHolder.LAYOUT
        else -> ShippingDurationViewHolder.ITEM_VIEW_SHIPMENT_DURATION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            LogisticPromoViewHolder.LAYOUT -> LogisticPromoViewHolder(view)
            else -> ShippingDurationViewHolder(view, this, cartPosition)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShippingDurationViewHolder -> holder.bindData(mData.get(position) as ShippingDurationViewModel, shippingDurationAdapterListener)
            is LogisticPromoViewHolder -> holder.bindData(mData.get(position) as LogisticPromoViewModel, shippingDurationAdapterListener!!)
        }
    }

    override fun getItemCount(): Int = mData.size

}
