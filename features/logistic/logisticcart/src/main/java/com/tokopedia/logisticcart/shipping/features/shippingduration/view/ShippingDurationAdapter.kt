package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel

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

    fun setShippingDurationViewModels(shippingDurationUiModels: List<ShippingDurationUiModel>, promoUiModel: LogisticPromoUiModel?, isDisableOrderPrioritas: Boolean) {
        this.isDisableOrderPrioritas = isDisableOrderPrioritas
        this.mData = shippingDurationUiModels.toMutableList()
        promoUiModel?.let { this.mData.add(0, it) }
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

    override fun getItemViewType(position: Int): Int = when (mData.get(position)) {
        is LogisticPromoUiModel -> ArmyViewHolder.LAYOUT
        is NotifierModel -> NotifierViewHolder.LAYOUT
        else -> ShippingDurationViewHolder.ITEM_VIEW_SHIPMENT_DURATION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            ArmyViewHolder.LAYOUT -> ArmyViewHolder(view)
            NotifierViewHolder.LAYOUT -> NotifierViewHolder(view)
            else -> ShippingDurationViewHolder(view, cartPosition)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShippingDurationViewHolder -> holder.bindData(mData.get(position) as ShippingDurationUiModel, shippingDurationAdapterListener, isDisableOrderPrioritas)
            is ArmyViewHolder -> holder.bindData(mData.get(position) as LogisticPromoUiModel, shippingDurationAdapterListener!!)
        }
    }

    override fun getItemCount(): Int = mData.size

}
