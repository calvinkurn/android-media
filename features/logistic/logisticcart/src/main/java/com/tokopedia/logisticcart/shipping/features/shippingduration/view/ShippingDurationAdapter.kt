package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.shipping.model.*

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

    fun setShippingDurationViewModels(shippingDurationUiModels: List<ShippingDurationUiModel>, promoUiModel: List<LogisticPromoUiModel>, isDisableOrderPrioritas: Boolean, preOrderModel: PreOrderModel?) {
        this.isDisableOrderPrioritas = isDisableOrderPrioritas
        this.mData = shippingDurationUiModels.toMutableList()
        if (preOrderModel?.display == true)  {
            preOrderModel.let { this.mData.add(0, it) }
            promoUiModel.mapIndexed { index, logisticPromoUiModel ->  this.mData.add(1 + index, logisticPromoUiModel) }
            // todo add divider here
        } else {
            promoUiModel.mapIndexed { index, logisticPromoUiModel ->  this.mData.add(index, logisticPromoUiModel) }
            // todo add divider here
        }
        if (shippingDurationUiModels[0].etaErrorCode == 1) {
            this.mData.add(0, NotifierModel())
        }
        notifyDataSetChanged()
    }

    fun setShippingDurationAdapterListener(shippingDurationAdapterListener: ShippingDurationAdapterListener) {
        this.shippingDurationAdapterListener = shippingDurationAdapterListener
    }

    fun setCartPosition(cartPosition: Int) {
        this.cartPosition = cartPosition
    }

    fun getRatesDataFromLogisticPromo(serId: Int): ShippingDurationUiModel? {
        mData.firstOrNull { it is ShippingDurationUiModel && it.serviceData.serviceId == serId }
                ?.let {
                    if (it is ShippingDurationUiModel) {
                        return it
                    }
                }
        return null
    }

    fun initiateShowcase() {
        var position = 0
        for (mDatum in mData) {
            if (mDatum is ShippingDurationUiModel) {
                mDatum.isShowShowCase = true
                break
            }
            position++
        }
        notifyItemChanged(position)
    }

    override fun getItemViewType(position: Int): Int = when (mData[position]) {
        is PreOrderModel -> PreOrderViewHolder.LAYOUT
        is LogisticPromoUiModel -> ArmyViewHolder.LAYOUT
        is NotifierModel -> NotifierViewHolder.LAYOUT
        else -> ShippingDurationViewHolder.ITEM_VIEW_SHIPMENT_DURATION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            PreOrderViewHolder.LAYOUT -> PreOrderViewHolder(view)
            ArmyViewHolder.LAYOUT -> ArmyViewHolder(view)
            NotifierViewHolder.LAYOUT -> NotifierViewHolder(view)
            else -> ShippingDurationViewHolder(view, cartPosition)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PreOrderViewHolder -> holder.bindData(mData[position] as PreOrderModel)
            is ShippingDurationViewHolder -> holder.bindData(mData[position] as ShippingDurationUiModel, shippingDurationAdapterListener, isDisableOrderPrioritas)
            is ArmyViewHolder -> holder.bindData(mData[position] as LogisticPromoUiModel, shippingDurationAdapterListener)
        }
    }

    override fun getItemCount(): Int = mData.size

}
