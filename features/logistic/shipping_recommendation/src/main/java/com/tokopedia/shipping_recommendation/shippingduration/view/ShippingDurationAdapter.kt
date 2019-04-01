package com.tokopedia.shipping_recommendation.shippingduration.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.shipping_recommendation.domain.shipping.LogisticPromoViewModel
import com.tokopedia.shipping_recommendation.domain.shipping.RatesViewModelType

import com.tokopedia.shipping_recommendation.domain.shipping.ShippingDurationViewModel

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

class ShippingDurationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mData: MutableList<RatesViewModelType>
    private var shippingDurationAdapterListener: ShippingDurationAdapterListener? = null
    private var cartPosition: Int = 0
    // set true if has courier promo, whether own courier or other duration's courier
    private var hasCourierPromo: Boolean = false

    init {
        mData = mutableListOf()
    }

    fun setShippingDurationViewModels(shippingDurationViewModels: List<ShippingDurationViewModel>) {
        this.mData = shippingDurationViewModels.toMutableList()
        this.mData.add(0, LogisticPromoViewModel(1))
        notifyDataSetChanged()
    }

    fun setShippingDurationAdapterListener(shippingDurationAdapterListener: ShippingDurationAdapterListener) {
        this.shippingDurationAdapterListener = shippingDurationAdapterListener
    }

    fun setCartPosition(cartPosition: Int) {
        this.cartPosition = cartPosition
    }

    fun setHasCourierPromo(hasCourierPromo: Boolean) {
        this.hasCourierPromo = hasCourierPromo
    }

    override fun getItemViewType(position: Int): Int = when (mData.get(position)) {
        is LogisticPromoViewModel -> LogisticPromoViewHolder.LAYOUT
        else -> ShippingDurationViewHolder.ITEM_VIEW_SHIPMENT_DURATION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            LogisticPromoViewHolder.LAYOUT -> LogisticPromoViewHolder(view)
            else -> ShippingDurationViewHolder(view, this, cartPosition, hasCourierPromo)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShippingDurationViewHolder -> holder.bindData(mData.get(position) as ShippingDurationViewModel, shippingDurationAdapterListener)
            is LogisticPromoViewHolder -> holder.bindData(mData.get(position) as LogisticPromoViewModel)
        }
    }

    override fun getItemCount(): Int = mData.size

}
