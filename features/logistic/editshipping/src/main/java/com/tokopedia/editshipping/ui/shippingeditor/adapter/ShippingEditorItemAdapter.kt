package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.FeatureInfoModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperModel
import com.tokopedia.editshipping.util.EditShippingConstant
import com.tokopedia.editshipping.util.EditShippingConstant.KURIR_REKOMENDASI_SHIPPER_ID
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback
import com.tokopedia.unifyprinciples.Typography

class ShippingEditorItemAdapter(private val listener: ShippingEditorItemAdapterListener, private val productItemListener: ShipperProductItemAdapter.ShipperProductItemListener) : RecyclerView.Adapter<ShippingEditorItemAdapter.ShippingEditorOnDemandViewHolder>() {

    var shipperModels = mutableListOf<ShipperModel>()

    interface ShippingEditorItemAdapterListener {
        fun onFeatureInfoClicked(data: List<FeatureInfoModel>)
        fun onShipperTickerClicked(data: ShipperModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingEditorOnDemandViewHolder {
        return ShippingEditorOnDemandViewHolder(parent.inflateLayout(R.layout.item_shipping_editor_card), listener, productItemListener)
    }

    override fun getItemCount(): Int {
        return shipperModels.size
    }

    override fun onBindViewHolder(holder: ShippingEditorOnDemandViewHolder, position: Int) {
        holder.bindData(shipperModels[position])
    }

    fun updateData(data: List<ShipperModel>) {
        shipperModels.clear()
        shipperModels.addAll(data)
        notifyDataSetChanged()
    }

    fun getActiveSpIds(): List<String> {
        val activatedListIds = mutableListOf<String>()
        shipperModels.forEach { courier ->
            courier.shipperProduct.forEach { product ->
                if (product.isActive) {
                    activatedListIds.add(product.shipperProductId)
                }
            }
        }
        return activatedListIds
    }

    fun getWhitelabelServicePosition(): Int {
        for (i in shipperModels.indices) {
            if (shipperModels[i].isWhitelabel) {
                return i
            }
        }
        return RecyclerView.NO_POSITION
    }

    fun getFirstNormalServicePosition(): Int {
        for (i in shipperModels.indices) {
            if (!shipperModels[i].isWhitelabel && shipperModels[i].shipperId != KURIR_REKOMENDASI_SHIPPER_ID.toLong()) {
                return i
            }
        }
        return RecyclerView.NO_POSITION
    }

    inner class ShippingEditorOnDemandViewHolder(itemView: View, private val listener: ShippingEditorItemAdapterListener, private val productItemListener: ShipperProductItemAdapter.ShipperProductItemListener) : RecyclerView.ViewHolder(itemView) {
        private val productItemAdapter = ShipperProductItemAdapter(productItemListener)
        private val featureItemAdapter = ShipperFeatureAdapter()
        private val shipmentItemImage = itemView.findViewById<ImageView>(R.id.img_shipment_item)
        private val shipmentName = itemView.findViewById<Typography>(R.id.shipment_name)
        private val shipmentItemCb = itemView.findViewById<CheckboxUnify>(R.id.cb_shipment_item)
        private val shipmentCategory = itemView.findViewById<Typography>(R.id.shipment_category)
        private val shipmentProductRv = itemView.findViewById<RecyclerView>(R.id.shipment_item_list)
        private val tickerShipper = itemView.findViewById<Ticker>(R.id.ticker_shipper)
        private val couponLayout = itemView.findViewById<FrameLayout>(R.id.layout_coupon)
        private val couponText = itemView.findViewById<Typography>(R.id.title_coupon)
        private val childLayout = itemView.findViewById<FrameLayout>(R.id.item_child_layout)
        private val flDisableContainer = itemView.findViewById<FrameLayout>(R.id.fl_container)
        private val shipmentFeatureRv = itemView.findViewById<RecyclerView>(R.id.rv_shipment_label)
        private val labelInformation = itemView.findViewById<IconUnify>(R.id.btn_information)

        fun bindData(data: ShipperModel) {
            setItemData(data)
            if (data.isWhitelabel) {
                hideShipperServices()
                setWhitelabelCheckListener(data)
            } else {
                setAdapterData(data)
                setItemChecked(data)
            }
        }

        private fun setWhitelabelCheckListener(data: ShipperModel) {
            setCheckboxEnableState(data)

            shipmentItemCb.isChecked = data.isActive

            shipmentItemCb.setOnCheckedChangeListener { _, isChecked ->
                data.isActive = isChecked
                data.shipperProduct.filter { it.isAvailable }.forEach { it.isActive = isChecked }
            }
        }

        private fun setCheckboxEnableState(data: ShipperModel) {
            if (!data.isAvailable) {
                flDisableContainer.foreground = ContextCompat.getDrawable(itemView.context, R.drawable.fg_disabled_item_log)
                shipmentItemCb.isEnabled = false
            } else {
                flDisableContainer.foreground = ContextCompat.getDrawable(itemView.context, R.drawable.fg_enabled_item_log)
                shipmentItemCb.isEnabled = true
            }
        }

        private fun hideShipperServices() {
            childLayout.gone()
        }

        private fun setItemData(data: ShipperModel) {
            shipmentName.text = data.shipperName
            shipmentCategory.text = data.description

            if (data.image.isNotEmpty()) {
                shipmentItemImage?.let {
                    ImageHandler.loadImageFitCenter(itemView.context, it, data.image)
                }
            } else {
                shipmentItemImage.gone()
            }

            if (data.textPromo.isEmpty()) {
                couponLayout.visibility = View.GONE
            } else {
                couponLayout.visibility = View.VISIBLE
                couponText.text = data.textPromo
            }

            when (data.tickerState) {
                EditShippingConstant.TICKER_STATE_ERROR -> {
                    tickerShipper.visibility = View.VISIBLE
                    tickerShipper.tickerType = Ticker.TYPE_ERROR
                    tickerShipper.setHtmlDescription(itemView.context.getString(R.string.shipper_ticker_red))
                }
                EditShippingConstant.TICKER_STATE_WARNING -> {
                    tickerShipper.visibility = View.VISIBLE
                    tickerShipper.tickerType = Ticker.TYPE_WARNING
                    tickerShipper.setHtmlDescription(itemView.context.getString(R.string.shipper_ticker_yellow, data.warehouseModel?.size))
                    tickerShipper.setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            listener.onShipperTickerClicked(data)
                        }

                        override fun onDismiss() {
                            // no-op
                        }
                    })
                }
                else -> {
                    tickerShipper.visibility = View.GONE
                }
            }

            setFeatureInfo(data.featureInfo)
        }

        private fun setFeatureInfo(featureInfo: List<FeatureInfoModel>) {
            if (featureInfo.isEmpty()) {
                shipmentFeatureRv?.gone()
                labelInformation?.gone()
            } else {
                shipmentFeatureRv?.visible()
                shipmentFeatureRv?.layoutManager = FlexboxLayoutManager(itemView.context).apply {
                    alignItems = AlignItems.FLEX_START
                }
                shipmentFeatureRv?.adapter = featureItemAdapter
                featureItemAdapter.setData(featureInfo)

                labelInformation?.visible()
                labelInformation?.setOnClickListener {
                    listener.onFeatureInfoClicked(featureInfo)
                }
            }
        }

        private fun setAdapterData(data: ShipperModel) {
            shipmentProductRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = productItemAdapter
            }

            productItemAdapter.addData(data.shipperProduct)

            initUncheckedListener()
        }

        private fun setItemChecked(data: ShipperModel) {
            setCheckboxEnableState(data)

            shipmentItemCb.isChecked = data.isActive
            if (shipmentItemCb.isChecked) {
                childLayout.visible()
            } else {
                childLayout.gone()
            }

            shipmentItemCb.setOnCheckedChangeListener { _, isChecked ->
                data.isActive = isChecked
                productItemAdapter?.updateChecked(isChecked)
                if (isChecked) {
                    childLayout.visible()
                } else {
                    childLayout.gone()
                }
            }
        }

        private fun initUncheckedListener() {
            productItemAdapter.setupUncheckedListener(object : ShipperProductItemAdapter.ShipperProductUncheckedListener {
                override fun uncheckedProduct() {
                    shipmentItemCb.isChecked = false
                }
            })
        }
    }
}
