package com.tokopedia.editshipping.ui.shippingeditor.adapter.viewholder_unused

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.ConventionalModel
import com.tokopedia.editshipping.domain.model.shippingEditor.OnDemandModel
import com.tokopedia.editshipping.domain.model.shippingEditor.ShippingEditorVisitable
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShipperProductItemAdapter
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ShippingEditorAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val data = mutableListOf<ShippingEditorVisitable>()
    private var shipperProductChild: ShipperProductItemAdapter? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ShippingEditorViewHolder(parent.inflateLayout(R.layout.item_shipping_editor_card))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        when (holder) {
            is ShippingEditorViewHolder -> {
                if (item is ConventionalModel) {
                    holder.bindConventional(item)
                } else if (item is OnDemandModel){
                    holder.bindOnDemand(item)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int = when(data[position]) {
        is OnDemandModel -> R.layout.item_shipping_editor_card
        is ConventionalModel -> R.layout.item_shipping_editor_card
    }

    fun setData(onDemand: List<OnDemandModel>, Conventional: List<ConventionalModel>) {
        data.addAll(onDemand)
        data.addAll(Conventional)
        notifyDataSetChanged()
    }


    inner class ShippingEditorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ShipperProductItemAdapter.ShipperProductOnDemandItemListener {
        private val shipmentItemImage = itemView.findViewById<ImageView>(R.id.img_shipment_item)
        private val shipmentName = itemView.findViewById<Typography>(R.id.shipment_name)
        private val shipmentItemCb = itemView.findViewById<CheckboxUnify>(R.id.cb_shipment_item)
        private val shipmentCategory = itemView.findViewById<Typography>(R.id.shipment_category)
        private val shipmentProductRv = itemView.findViewById<RecyclerView>(R.id.shipment_item_list)

        fun bindConventional(data: ConventionalModel) {
            setItemDataConventional(data)
        }

        private fun setItemDataConventional(data: ConventionalModel) {
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

            shipperProductChild = ShipperProductItemAdapter(this@ShippingEditorViewHolder)
            shipmentProductRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = shipperProductChild
            }

            shipperProductChild?.addData(data.shipperProduct)

        }

        override fun onShipperProductItemClicked() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        fun bindOnDemand(data: OnDemandModel) {
            setItemDataOnDemand(data)
        }

        private fun setItemDataOnDemand(data: OnDemandModel) {
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

            shipperProductChild = ShipperProductItemAdapter(this@ShippingEditorViewHolder)
            shipmentProductRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = shipperProductChild
            }

            shipperProductChild?.addData(data.shipperProduct)

        }


        /* private fun setListener(data: OnDemandModel) {
            shipmentItemCb.setOnCheckedChangeListener { _, isChecked ->
                listener.onShipperInfoClicked()
            }
        }*/
    }

}