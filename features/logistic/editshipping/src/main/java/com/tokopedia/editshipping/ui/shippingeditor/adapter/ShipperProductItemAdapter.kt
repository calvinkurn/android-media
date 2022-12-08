package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.ShipperProductModel
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ShipperProductItemAdapter(private var listener: ShipperProductItemListener) : RecyclerView.Adapter<ShipperProductItemAdapter.ShipperProductOnDemandViewHolder>() {

    interface ShipperProductItemListener

    interface ShipperProductUncheckedListener {
        fun uncheckedProduct()
    }

    fun setupUncheckedListener(listener: ShipperProductUncheckedListener) {
        this.shipperProductUncheckedListener = listener
    }

    private var shipperProductUncheckedListener: ShipperProductUncheckedListener? = null

    private var shipperProduct = mutableListOf<ShipperProductModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipperProductOnDemandViewHolder {
        return ShipperProductOnDemandViewHolder(parent.inflateLayout(R.layout.item_shipper_product_name))
    }

    override fun getItemCount(): Int {
        return shipperProduct.size
    }

    override fun onBindViewHolder(holder: ShipperProductOnDemandViewHolder, position: Int) {
        holder.bindData(shipperProduct[position])
    }

    override fun onViewRecycled(holder: ShipperProductOnDemandViewHolder) {
        super.onViewRecycled(holder)
        holder.shipperProductCb.setOnCheckedChangeListener(null)
    }

    fun addData(data: List<ShipperProductModel>) {
        shipperProduct.clear()
        shipperProduct.addAll(data)
        notifyDataSetChanged()
    }

    fun updateChecked(checked: Boolean) {
        shipperProduct.filter { it.isAvailable }.forEach {
            it.isActive = checked
        }
        notifyDataSetChanged()
    }

    inner class ShipperProductOnDemandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val shipperProductName = itemView.findViewById<Typography>(R.id.shipper_product_name)
        val shipperProductCb = itemView.findViewById<CheckboxUnify>(R.id.shipper_product_cb)
        private val divider = itemView.findViewById<View>(R.id.divider_shipment)

        fun bindData(data: ShipperProductModel) {
            setItemData(data)
        }

        private fun setItemData(data: ShipperProductModel) {
            setShipperProductName(data)
            setShipperProductEnableState(data)
            setCheckBoxCheckedState(data)
            setDivider(data)
            setCheckBoxListener(data)
        }

        private fun setCheckBoxListener(data: ShipperProductModel) {
            shipperProductCb?.setOnCheckedChangeListener { _, isChecked ->
                data.isActive = isChecked
                activeProductChecker(isChecked)
            }
        }

        private fun setShipperProductName(data: ShipperProductModel) {
            shipperProductName.text = data.shipperProductName
        }

        private fun setDivider(data: ShipperProductModel) {
            val lastItem = shipperProduct.last()
            if (data == lastItem) {
                divider.visibility = View.GONE
            }
        }

        private fun setCheckBoxCheckedState(data: ShipperProductModel) {
            if (data.isAvailable) {
                shipperProductCb.isEnabled = true
                shipperProductCb.isChecked = data.isActive
            } else {
                shipperProductCb.isEnabled = false
            }
        }

        private fun setShipperProductEnableState(data: ShipperProductModel) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (data.isAvailable) {
                    itemView.rootView.foreground = MethodChecker.getDrawable(itemView.context, R.drawable.fg_enabled_item_log)
                } else {
                    itemView.rootView.foreground = MethodChecker.getDrawable(itemView.context, R.drawable.fg_disabled_item_log)
                }
            }
        }

        private fun activeProductChecker(isChecked: Boolean) {
            if (!isChecked) {
                val hasActiveItem = shipperProduct.find { it.isActive }
                if (hasActiveItem == null) {
                    shipperProductUncheckedListener?.uncheckedProduct()
                }
            }
        }
    }
}
