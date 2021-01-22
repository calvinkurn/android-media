/*
package com.tokopedia.editshipping.ui.shippingeditor.adapter.viewholder_unused

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.domain.model.shippingEditor.ConventionalModel
import com.tokopedia.editshipping.ui.shippingeditor.adapter.ShipperProductItemAdapter
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify
import com.tokopedia.unifyprinciples.Typography

class ShippingEditorConventionalViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), ShipperProductItemAdapter.ShipperProductOnDemandItemListener {

    private var shipperProductConventionalChild: ShipperProductItemAdapter? = null

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

}*/
