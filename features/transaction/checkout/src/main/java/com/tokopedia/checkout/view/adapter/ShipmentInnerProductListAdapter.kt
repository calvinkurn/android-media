package com.tokopedia.checkout.view.adapter

import com.tokopedia.logisticcart.shipping.model.CartItemModel
import com.tokopedia.checkout.view.viewholder.ShipmentCartItemViewHolder.ShipmentItemListener
import com.tokopedia.checkout.view.viewholder.ShipmentCartItemViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.checkout.R

class ShipmentInnerProductListAdapter(private val mCartItemList: List<CartItemModel>, private val mListener: ShipmentItemListener) : RecyclerView.Adapter<ShipmentCartItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShipmentCartItemViewHolder {
        val mContext = parent.context
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_cart_product, parent, false)
        return ShipmentCartItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShipmentCartItemViewHolder, position: Int) {
        val cartItemModel = mCartItemList[position]
        holder.bindViewHolder(cartItemModel, mListener)
    }

    override fun getItemCount(): Int {
        return mCartItemList.size
    }
}