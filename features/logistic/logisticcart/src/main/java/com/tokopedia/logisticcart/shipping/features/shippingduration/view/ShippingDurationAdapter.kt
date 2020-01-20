package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.shipping.model.LogisticPromoViewModel
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingDurationViewModel

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

class ShippingDurationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mData: MutableList<RatesViewModelType>
    private var shippingDurationAdapterListener: ShippingDurationAdapterListener? = null
    private var cartPosition: Int = 0
    private var isDisableOrderPrioritas = false

    init {
        mData = mutableListOf()
    }

    fun setShippingDurationViewModels(shippingDurationViewModels: List<ShippingDurationViewModel>, promoViewModel: LogisticPromoViewModel?, isDisableOrderPrioritas: Boolean) {
        this.isDisableOrderPrioritas = isDisableOrderPrioritas
        this.mData = shippingDurationViewModels.toMutableList()
        promoViewModel?.let { this.mData.add(0, it) }
        this.mData.add(0, NotifierModel())
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
        is LogisticPromoViewModel -> ArmyViewHolder.LAYOUT
        is NotifierModel -> NotifierViewHolder.LAYOUT
        else -> ShippingDurationViewHolder.ITEM_VIEW_SHIPMENT_DURATION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            ArmyViewHolder.LAYOUT -> ArmyViewHolder(view)
            NotifierViewHolder.LAYOUT -> NotifierViewHolder(view)
            else -> ShippingDurationViewHolder(view, this, cartPosition)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShippingDurationViewHolder -> holder.bindData(mData.get(position) as ShippingDurationViewModel, shippingDurationAdapterListener, isDisableOrderPrioritas)
            is ArmyViewHolder -> holder.bindData(mData.get(position) as LogisticPromoViewModel, shippingDurationAdapterListener!!)
        }
    }

    override fun getItemCount(): Int = mData.size

}
