package com.tokopedia.cart.view.adapter.collapsedproduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.databinding.ItemCartCollapsedProductBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartItemHolderData
import com.tokopedia.cart.view.viewholder.now.CartCollapsedProductViewHolder

class CartCollapsedProductAdapter(val actionListener: ActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var cartCollapsedProductHolderDataList: List<CartItemHolderData> = arrayListOf()
    var parentPosition: Int = RecyclerView.NO_POSITION

    override fun getItemViewType(position: Int): Int {
        return CartCollapsedProductViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCartCollapsedProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartCollapsedProductViewHolder(binding, actionListener, parentPosition)
    }

    override fun getItemCount(): Int {
        return cartCollapsedProductHolderDataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderView = holder as CartCollapsedProductViewHolder
        val data = cartCollapsedProductHolderDataList[position]
        holderView.bind(data)
    }

}