package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.ConventionalModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperTickerModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography

class ShippingEditorConventionalAdapter(private val listener: ShippingEditorConventionalListener) : RecyclerView.Adapter<ShippingEditorConventionalAdapter.ShippingEditorConventionalViewHolder>(){

    private var shipperConventionalModel = mutableListOf<ConventionalModel>()

    interface ShippingEditorConventionalListener {
        fun onShipperTickerConventionalClicked(data: ConventionalModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingEditorConventionalViewHolder {
        return ShippingEditorConventionalViewHolder(parent.inflateLayout(R.layout.item_shipping_editor_card), listener)
    }

    override fun getItemCount(): Int {
        return shipperConventionalModel.size
    }

    override fun onBindViewHolder(holder: ShippingEditorConventionalViewHolder, position: Int) {
        holder.binData(shipperConventionalModel[position])
    }

    override fun onViewRecycled(holder: ShippingEditorConventionalViewHolder) {
        super.onViewRecycled(holder)
        holder.shipmentItemCb.setOnCheckedChangeListener(null)
    }

    fun updateData(data: List<ConventionalModel>) {
        shipperConventionalModel.clear()
        shipperConventionalModel.addAll(data)
        notifyDataSetChanged()
    }

    fun setTickerData(data: ShipperTickerModel) {
        data.courierTicker.forEach {
           val conventionalModel = shipperConventionalModel.find { conventional ->
                conventional.shipperId == it.shipperId
            }
            conventionalModel?.tickerState = it.tickerState
            conventionalModel?.isAvailable = it.isAvailable
            conventionalModel?.warehouseModel = it.warehouses
        }
        notifyDataSetChanged()
    }

    fun getActiveSpIds(): List<String> {
        val activatedListIds = mutableListOf<String>()
        shipperConventionalModel.forEach { courier ->
            courier.shipperProduct.forEach { product ->
                if (product.isActive) {
                    activatedListIds.add(product.shipperProductId)
                }
            }

        }
        return activatedListIds
    }

    fun clearData() {
        shipperConventionalModel.clear()
        notifyDataSetChanged()
    }

    inner class ShippingEditorConventionalViewHolder(itemView: View, private val listener: ShippingEditorConventionalListener) : RecyclerView.ViewHolder(itemView) {
        lateinit var conventionalModel: ConventionalModel
        private val shipperProductConventionalChild = ShipperProductItemAdapter()
        private val shipmentItemImage = itemView.findViewById<ImageView>(R.id.img_shipment_item)
        private val shipmentName = itemView.findViewById<Typography>(R.id.shipment_name)
        val shipmentItemCb = itemView.findViewById<CheckboxUnify>(R.id.cb_shipment_item)
        private val shipmentCategory = itemView.findViewById<Typography>(R.id.shipment_category)
        private val shipmentProductRv = itemView.findViewById<RecyclerView>(R.id.shipment_item_list)
        private val tickerShipper = itemView.findViewById<Ticker>(R.id.ticker_shipper)
        private val couponLayout = itemView.findViewById<RelativeLayout>(R.id.layout_coupon)
        private val couponText = itemView.findViewById<Typography>(R.id.title_coupon)
        private val childLayout = itemView.findViewById<FrameLayout>(R.id.item_child_layout)

        fun binData(data: ConventionalModel) {
            conventionalModel = data
            setItemData(data)
            setItemChecked(data)
        }

        private fun setItemData(data: ConventionalModel) {
            val shipperName = data.shipperProduct
            var sb = StringBuilder()

            shipmentItemImage?.let {
                ImageHandler.loadImageFitCenter(itemView.context, it, data.image)
            }
            shipmentName.text = data.shipperName

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

            shipmentProductRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = shipperProductConventionalChild
            }

            shipperProductConventionalChild?.addData(data.shipperProduct)

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
                            listener.onShipperTickerConventionalClicked(data)
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

        private fun setItemChecked(data: ConventionalModel) {
            shipmentItemCb.isChecked = data.isActive
            if (shipmentItemCb.isChecked) {
                childLayout.visible()
            } else {
                childLayout.gone()
            }


            shipmentItemCb.setOnCheckedChangeListener { _, isChecked ->
                data.isActive = isChecked
                shipperProductConventionalChild?.updateChecked(isChecked)
                if (isChecked) {
                    childLayout.visible()
                } else {
                    childLayout.gone()
                }
            }
        }

        /* private fun setListener(data: OnDemandModel) {
            shipmentItemCb.setOnCheckedChangeListener { _, isChecked ->
                listener.onShipperInfoClicked()
            }
        }*/
    }

}