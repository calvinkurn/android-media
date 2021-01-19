package com.tokopedia.editshipping.ui.shippingeditor.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.editshipping.R
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.logisticCommon.data.entity.shippingeditor.ConventionalModel
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ShippingEditorConventionalAdapter() : RecyclerView.Adapter<ShippingEditorConventionalAdapter.ShippingEditorConventionalViewHolder>(){

    private var shipperConventionalModel = mutableListOf<ConventionalModel>()

    private var shipperProductConventionalChild: ShipperProductItemAdapter? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShippingEditorConventionalViewHolder {
        return ShippingEditorConventionalViewHolder(parent.inflateLayout(R.layout.item_shipping_editor_card))
    }

    override fun getItemCount(): Int {
        return shipperConventionalModel.size
    }

    override fun onBindViewHolder(holder: ShippingEditorConventionalViewHolder, position: Int) {
        holder.binData(shipperConventionalModel[position])
    }

    fun updateData(data: List<ConventionalModel>) {
        shipperConventionalModel.addAll(data)
        notifyDataSetChanged()
    }

    fun clearData() {
        shipperConventionalModel.clear()
        notifyDataSetChanged()
    }

    inner class ShippingEditorConventionalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ShipperProductItemAdapter.ShipperProductOnDemandItemListener {
        private val shipmentItemImage = itemView.findViewById<ImageView>(R.id.img_shipment_item)
        private val shipmentName = itemView.findViewById<Typography>(R.id.shipment_name)
        private val shipmentItemCb = itemView.findViewById<CheckboxUnify>(R.id.cb_shipment_item)
        private val shipmentCategory = itemView.findViewById<Typography>(R.id.shipment_category)
        private val shipmentProductRv = itemView.findViewById<RecyclerView>(R.id.shipment_item_list)

        fun binData(data: ConventionalModel) {
            setItemData(data)
        }

        private fun setItemData(data: ConventionalModel) {
            val shipperName = data.shipperProduct
            var sb = StringBuilder()

            shipmentItemImage?.let {
                ImageHandler.loadImageFitCenter(itemView.context, it, data.image)
            }
            shipmentName.text = data.shipperName
            shipmentItemCb.isChecked = data.isActive

            for (x in shipperName.indices) {
                sb.append(shipperName[x].shipperProductName).append(" | ")
            }

            shipmentCategory.text = sb.substring(0, sb.length - 2)

            shipperProductConventionalChild = ShipperProductItemAdapter(this@ShippingEditorConventionalViewHolder)
            shipmentProductRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = shipperProductConventionalChild
            }

            shipperProductConventionalChild?.addData(data.shipperProduct)

        }

        override fun onShipperProductItemClicked() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        /* private fun setListener(data: OnDemandModel) {
            shipmentItemCb.setOnCheckedChangeListener { _, isChecked ->
                listener.onShipperInfoClicked()
            }
        }*/
    }

}