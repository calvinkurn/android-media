package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.logisticcart.databinding.ItemArmyBinding
import com.tokopedia.logisticcart.databinding.ItemDividerLogisticCartBinding
import com.tokopedia.logisticcart.databinding.ItemDurationBinding
import com.tokopedia.logisticcart.databinding.ItemNotifierBinding
import com.tokopedia.logisticcart.databinding.ItemProductShipmentDetailBinding
import com.tokopedia.logisticcart.shipping.model.DividerModel
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.NotifierModel
import com.tokopedia.logisticcart.shipping.model.ProductShipmentDetailModel
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel

/**
 * Created by Irfan Khoirul on 08/08/18.
 */

class ShippingDurationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // list of eligible services that user can choose
    private var mData: MutableList<RatesViewModelType>
    private var shippingDurationAdapterListener: ShippingDurationAdapterListener? = null
    private var cartPosition: Int = 0
    private var isDisableOrderPrioritas = false

    init {
        mData = mutableListOf()
    }

    fun setShippingDurationViewModels(shippingDurationUiModels: MutableList<RatesViewModelType>, isDisableOrderPrioritas: Boolean) {
        this.isDisableOrderPrioritas = isDisableOrderPrioritas
        this.mData = shippingDurationUiModels
        notifyDataSetChanged()
    }

    fun setShippingDurationAdapterListener(shippingDurationAdapterListener: ShippingDurationAdapterListener) {
        this.shippingDurationAdapterListener = shippingDurationAdapterListener
    }

    fun setCartPosition(cartPosition: Int) {
        this.cartPosition = cartPosition
    }

    override fun getItemViewType(position: Int): Int = when (mData[position]) {
        is LogisticPromoUiModel -> ArmyViewHolder.LAYOUT
        is NotifierModel -> NotifierViewHolder.LAYOUT
        is DividerModel -> DividerViewHolder.LAYOUT
        is ProductShipmentDetailModel -> ProductShipmentDetailViewHolder.LAYOUT
        else -> ShippingDurationViewHolder.ITEM_VIEW_SHIPMENT_DURATION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ArmyViewHolder.LAYOUT -> ArmyViewHolder(ItemArmyBinding.inflate(inflater, parent, false))
            NotifierViewHolder.LAYOUT -> NotifierViewHolder(
                ItemNotifierBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )

            DividerViewHolder.LAYOUT -> DividerViewHolder(
                ItemDividerLogisticCartBinding.inflate(
                    inflater,
                    parent,
                    false
                )
            )

            ProductShipmentDetailViewHolder.LAYOUT -> ProductShipmentDetailViewHolder(
                ItemProductShipmentDetailBinding.inflate(inflater, parent, false)
            )

            else -> ShippingDurationViewHolder(
                ItemDurationBinding.inflate(inflater, parent, false),
                cartPosition
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShippingDurationViewHolder -> holder.bindData(mData[position] as ShippingDurationUiModel, shippingDurationAdapterListener, isDisableOrderPrioritas)
            is ArmyViewHolder -> holder.bindData(mData[position] as LogisticPromoUiModel, shippingDurationAdapterListener)
            is NotifierViewHolder -> holder.bindData(mData[position] as NotifierModel)
            is ProductShipmentDetailViewHolder -> holder.bindData(mData[position] as ProductShipmentDetailModel)
        }
    }

    override fun getItemCount(): Int = mData.size
}
