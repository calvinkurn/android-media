package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.OnDemandModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperTickerModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography

class ShippingEditorOnDemandItemAdapter(private val listener: ShippingEditorItemAdapterListener): RecyclerView.Adapter<ShippingEditorOnDemandItemAdapter.ShippingEditorOnDemandViewHolder>() {

    var shipperOnDemandModel = mutableListOf<OnDemandModel>()

    interface ShippingEditorItemAdapterListener {
        fun onShipperInfoClicked()
        fun onShipperTickerOnDemandClicked(data: OnDemandModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingEditorOnDemandViewHolder {
        return ShippingEditorOnDemandViewHolder(parent.inflateLayout(R.layout.item_shipping_editor_card), listener)
    }

    override fun getItemCount(): Int {
        return shipperOnDemandModel.size
    }

    override fun onBindViewHolder(holder: ShippingEditorOnDemandViewHolder, position: Int) {
        holder.bindData(shipperOnDemandModel[position])
    }

    fun updateData(data: List<OnDemandModel>) {
        shipperOnDemandModel.clear()
        shipperOnDemandModel.addAll(data)
        notifyDataSetChanged()
    }

    fun setTickerData(data: ShipperTickerModel) {
        data.courierTicker.forEach {
            val onDemandModel = shipperOnDemandModel.find { onDemand ->
                onDemand.shipperId == it.shipperId
            }
            onDemandModel?.tickerState = it.tickerState
            onDemandModel?.isAvailable = it.isAvailable
            onDemandModel?.warehouseModel = it.warehouses
        }
        notifyDataSetChanged()
    }

    fun getActiveSpIds(): List<String> {
        val activatedListIds = mutableListOf<String>()
        shipperOnDemandModel.forEach { courier ->
            courier.shipperProduct.forEach { product ->
                if (product.isActive) {
                    activatedListIds.add(product.shipperProductId)
                }
            }

        }
        return activatedListIds
    }

    fun clearData() {
        shipperOnDemandModel.clear()
        notifyDataSetChanged()
    }

    inner class ShippingEditorOnDemandViewHolder(itemView: View, private val listener: ShippingEditorItemAdapterListener): RecyclerView.ViewHolder(itemView) {
        lateinit var onDemandModel: OnDemandModel
        private var shipperProductChild = ShipperProductItemAdapter()
        private val shipmentItemImage = itemView.findViewById<ImageView>(R.id.img_shipment_item)
        private val shipmentName = itemView.findViewById<Typography>(R.id.shipment_name)
        private val shipmentItemCb = itemView.findViewById<CheckboxUnify>(R.id.cb_shipment_item)
        private val shipmentCategory = itemView.findViewById<Typography>(R.id.shipment_category)
        private val shipmentProductRv = itemView.findViewById<RecyclerView>(R.id.shipment_item_list)
        private val tickerShipper = itemView.findViewById<Ticker>(R.id.ticker_shipper)
        private val couponLayout = itemView.findViewById<RelativeLayout>(R.id.layout_coupon)
        private val couponText = itemView.findViewById<Typography>(R.id.title_coupon)
        private val childLayout = itemView.findViewById<FrameLayout>(R.id.item_child_layout)
        private val flDisableContainer = itemView.findViewById<FrameLayout>(R.id.fl_container)

        fun bindData(data: OnDemandModel) {
            onDemandModel = data
            setItemData(data)
            setItemChecked(data)
        }

        private fun setItemData(data: OnDemandModel) {
            val shipperName = data.shipperProduct
            var sb = StringBuilder()

            shipmentName.text = data.shipperName
            shipmentItemImage?.let {
                ImageHandler.loadImageFitCenter(itemView.context, it, data.image)
            }

            for (x in shipperName.indices) {
                sb.append(shipperName[x].shipperProductName).append(" | ")
            }
            shipmentCategory.text = sb.substring(0, sb.length - 2)

            if(data.textPromo.isEmpty()) {
                couponLayout.visibility = View.GONE
            } else {
                couponLayout.visibility = View.VISIBLE
                couponText.text = data.textPromo
            }

            shipperProductChild = ShipperProductItemAdapter()
            shipmentProductRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = shipperProductChild
            }
            shipperProductChild?.addData(data.shipperProduct)

            when (data.tickerState) {
                1 -> {
                    tickerShipper.visibility = View.VISIBLE
                    tickerShipper.tickerType = Ticker.TYPE_ERROR
                    tickerShipper.setHtmlDescription(itemView.context.getString(R.string.shipper_ticker_red))
                }
                2 -> {
                    tickerShipper.visibility = View.VISIBLE
                    tickerShipper.tickerType = Ticker.TYPE_WARNING
                    tickerShipper.setHtmlDescription(itemView.context.getString(R.string.shipper_ticker_yellow, data.warehouseModel?.size))
                    tickerShipper.setDescriptionClickEvent(object: TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            listener.onShipperTickerOnDemandClicked(data)
                        }

                        override fun onDismiss() {
                            //no-op
                        }

                    })
                }
                else -> {
                    tickerShipper.visibility = View.GONE
                }
            }


        }

        private fun setItemChecked(data: OnDemandModel) {
            shipmentItemCb.isChecked = data.isActive
            if (shipmentItemCb.isChecked) {
                childLayout.visible()
            } else {
                childLayout.gone()
            }

            if (data.tickerState == 1) {
                flDisableContainer.foreground = ContextCompat.getDrawable(itemView.context, R.drawable.fg_disabled_item_log)
                shipmentItemCb.isEnabled = false
            } else {
                flDisableContainer.foreground = ContextCompat.getDrawable(itemView.context, R.drawable.fg_enabled_item_log)
                shipmentItemCb.isEnabled = true
                shipmentItemCb.setOnCheckedChangeListener { _, isChecked ->
                    data.isActive = isChecked
                    shipperProductChild?.updateChecked(isChecked)
                    if (isChecked) {
                        childLayout.visible()
                    } else {
                        childLayout.gone()
                    }
                }
            }
        }

    }

}