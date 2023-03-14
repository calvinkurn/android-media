package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.databinding.ItemShippingEditorCardBinding
import com.tokopedia.editshipping.domain.model.shippingEditor.FeatureInfoModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperModel
import com.tokopedia.editshipping.util.EditShippingConstant
import com.tokopedia.editshipping.util.EditShippingConstant.KURIR_REKOMENDASI_SHIPPER_ID
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImageFitCenter
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerCallback

class ShippingEditorItemAdapter(
    private val listener: ShippingEditorItemAdapterListener,
    private val productItemListener: ShipperProductItemAdapter.ShipperProductItemListener
) : RecyclerView.Adapter<ShippingEditorItemAdapter.ShippingEditorOnDemandViewHolder>() {

    var shipperModels = mutableListOf<ShipperModel>()

    interface ShippingEditorItemAdapterListener {
        fun onFeatureInfoClicked(data: List<FeatureInfoModel>)
        fun onShipperTickerClicked(data: ShipperModel)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShippingEditorOnDemandViewHolder {
        return ShippingEditorOnDemandViewHolder(
            ItemShippingEditorCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            listener,
            productItemListener
        )
    }

    override fun getItemCount(): Int {
        return shipperModels.size
    }

    override fun onBindViewHolder(holder: ShippingEditorOnDemandViewHolder, position: Int) {
        holder.bindData(shipperModels[position])
    }

    @SuppressLint("NotifyDataSetChanged")
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

    inner class ShippingEditorOnDemandViewHolder(
        private val binding: ItemShippingEditorCardBinding,
        private val listener: ShippingEditorItemAdapterListener,
        private val productItemListener: ShipperProductItemAdapter.ShipperProductItemListener
    ) : RecyclerView.ViewHolder(binding.root) {
        private val productItemAdapter = ShipperProductItemAdapter(productItemListener)
        private val featureItemAdapter = ShipperFeatureAdapter()
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

            binding.cbShipmentItem.isChecked = data.isActive

            binding.cbShipmentItem.setOnCheckedChangeListener { _, isChecked ->
                data.isActive = isChecked
                data.shipperProduct.filter { it.isAvailable }.forEach { it.isActive = isChecked }
            }
        }

        private fun setCheckboxEnableState(data: ShipperModel) {
            if (!data.isAvailable) {
                binding.flContainer.foreground =
                    ContextCompat.getDrawable(itemView.context, R.drawable.fg_disabled_item_log)
                binding.cbShipmentItem.isEnabled = false
            } else {
                binding.flContainer.foreground =
                    ContextCompat.getDrawable(itemView.context, R.drawable.fg_enabled_item_log)
                binding.cbShipmentItem.isEnabled = true
            }
        }

        private fun hideShipperServices() {
            binding.itemChildLayout.gone()
        }

        private fun setItemData(data: ShipperModel) {
            binding.shipmentName.text = data.shipperName
            binding.shipmentCategory.text = data.description

            if (data.image.isNotEmpty()) {
                binding.imgShipmentItem.loadImageFitCenter(data.image)
            } else {
                binding.imgShipmentItem.gone()
            }

            if (data.textPromo.isEmpty()) {
                binding.layoutCoupon.root.visibility = View.GONE
            } else {
                binding.layoutCoupon.root.visibility = View.VISIBLE
                binding.layoutCoupon.titleCoupon.text = data.textPromo
            }

            when (data.tickerState) {
                EditShippingConstant.TICKER_STATE_ERROR -> {
                    binding.tickerShipper.visibility = View.VISIBLE
                    binding.tickerShipper.tickerType = Ticker.TYPE_ERROR
                    binding.tickerShipper.setHtmlDescription(itemView.context.getString(R.string.shipper_ticker_red))
                }
                EditShippingConstant.TICKER_STATE_WARNING -> {
                    binding.tickerShipper.visibility = View.VISIBLE
                    binding.tickerShipper.tickerType = Ticker.TYPE_WARNING
                    binding.tickerShipper.setHtmlDescription(
                        itemView.context.getString(
                            R.string.shipper_ticker_yellow,
                            data.warehouseModel.size
                        )
                    )
                    binding.tickerShipper.setDescriptionClickEvent(object : TickerCallback {
                        override fun onDescriptionViewClick(linkUrl: CharSequence) {
                            listener.onShipperTickerClicked(data)
                        }

                        override fun onDismiss() {
                            // no-op
                        }
                    })
                }
                else -> {
                    binding.tickerShipper.visibility = View.GONE
                }
            }

            setFeatureInfo(data.featureInfo)
        }

        private fun setFeatureInfo(featureInfo: List<FeatureInfoModel>) {
            if (featureInfo.isEmpty()) {
                binding.rvShipmentLabel.gone()
                binding.btnInformation.gone()
            } else {
                binding.rvShipmentLabel.visible()
                binding.rvShipmentLabel.layoutManager =
                    FlexboxLayoutManager(itemView.context).apply {
                        alignItems = AlignItems.FLEX_START
                    }
                binding.rvShipmentLabel.adapter = featureItemAdapter
                featureItemAdapter.setData(featureInfo)

                binding.btnInformation.visible()
                binding.btnInformation.setOnClickListener {
                    listener.onFeatureInfoClicked(featureInfo)
                }
            }
        }

        private fun setAdapterData(data: ShipperModel) {
            binding.shipmentItemList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = productItemAdapter
            }

            productItemAdapter.addData(data.shipperProduct)

            initUncheckedListener()
        }

        private fun setItemChecked(data: ShipperModel) {
            setCheckboxEnableState(data)

            binding.cbShipmentItem.isChecked = data.isActive
            if (binding.cbShipmentItem.isChecked) {
                binding.itemChildLayout.visible()
            } else {
                binding.itemChildLayout.gone()
            }

            binding.cbShipmentItem.setOnCheckedChangeListener { _, isChecked ->
                data.isActive = isChecked
                productItemAdapter.updateChecked(isChecked)
                if (isChecked) {
                    binding.itemChildLayout.visible()
                } else {
                    binding.itemChildLayout.gone()
                }
            }
        }

        private fun initUncheckedListener() {
            productItemAdapter.setupUncheckedListener(object :
                    ShipperProductItemAdapter.ShipperProductUncheckedListener {
                    override fun uncheckedProduct() {
                        binding.cbShipmentItem.isChecked = false
                    }
                })
        }
    }
}
