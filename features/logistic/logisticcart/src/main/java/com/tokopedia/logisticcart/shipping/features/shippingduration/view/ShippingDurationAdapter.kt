package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.shipping.model.*

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

class ShippingDurationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // list of eligible services that user can choose
    private var mData: MutableList<RatesViewModelType>
    // list of all available services from rates (for logistic promo)
    private var allServices: List<ShippingDurationUiModel>
    private var shippingDurationAdapterListener: ShippingDurationAdapterListener? = null
    private var cartPosition: Int = 0
    private var isDisableOrderPrioritas = false

    init {
        mData = mutableListOf()
        allServices = mutableListOf()
    }

    fun setShippingDurationViewModels(shippingDurationUiModels: List<ShippingDurationUiModel>, promoUiModel: List<LogisticPromoUiModel>, isDisableOrderPrioritas: Boolean, preOrderModel: PreOrderModel?) {
        this.isDisableOrderPrioritas = isDisableOrderPrioritas
        this.allServices = shippingDurationUiModels
        this.mData = shippingDurationUiModels.filter { !it.serviceData.isUiRatesHidden }.toMutableList()
        if (preOrderModel?.display == true)  {
            preOrderModel.let { this.mData.add(0, it) }
            if (promoUiModel.isNotEmpty()) {
                this.mData.addAll(1, promoUiModel + listOf<RatesViewModelType>(DividerModel()))
            }
        } else {
            if (promoUiModel.isNotEmpty()) {
                this.mData.addAll(0, promoUiModel + listOf<RatesViewModelType>(DividerModel()))
            }
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
        allServices.firstOrNull { it.serviceData.serviceId == serId }
            ?.let {
                return it
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
        is DividerModel -> DividerViewHolder.LAYOUT
        else -> ShippingDurationViewHolder.ITEM_VIEW_SHIPMENT_DURATION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return when (viewType) {
            PreOrderViewHolder.LAYOUT -> PreOrderViewHolder(view)
            ArmyViewHolder.LAYOUT -> ArmyViewHolder(view)
            NotifierViewHolder.LAYOUT -> NotifierViewHolder(view)
            DividerViewHolder.LAYOUT -> DividerViewHolder(view)
            else -> ShippingDurationViewHolder(view, cartPosition)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PreOrderViewHolder -> holder.bindData(mData[position] as PreOrderModel)
            is ShippingDurationViewHolder -> holder.bindData(mData[position] as ShippingDurationUiModel, shippingDurationAdapterListener, isDisableOrderPrioritas)
            is ArmyViewHolder -> holder.bindData(
                mData[position] as LogisticPromoUiModel,
                shippingDurationAdapterListener
            )
        }
    }

    override fun getItemCount(): Int = mData.size

}
