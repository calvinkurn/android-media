package com.tokopedia.checkout.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.viewholder.ShipmentCartItemViewHolder
import com.tokopedia.checkout.view.viewholder.ShipmentCartItemViewHolder.ShipmentItemListener
import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.purchase_platform.common.feature.gifting.data.model.AddOnWordingModel

class ShipmentInnerProductListAdapter(private val mCartItemList: MutableList<CartItemModel>, private val addOnWordingModel: AddOnWordingModel, private val mListener: ShipmentItemListener) : RecyclerView.Adapter<ShipmentCartItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipmentCartItemViewHolder {
        val mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_shipment_product, parent, false)
        return ShipmentCartItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShipmentCartItemViewHolder, position: Int) {
        val cartItemModel = mCartItemList[position]
        holder.bindViewHolder(cartItemModel, addOnWordingModel, mListener)
    }

    override fun getItemCount(): Int {
        return mCartItemList.size
    }
}
