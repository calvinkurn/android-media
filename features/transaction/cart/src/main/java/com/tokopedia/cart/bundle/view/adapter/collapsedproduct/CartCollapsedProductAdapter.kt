package com.tokopedia.cart.bundle.view.adapter.collapsedproduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.databinding.ItemCartCollapsedProductBundleBinding
import com.tokopedia.cart.bundle.view.ActionListener
import com.tokopedia.cart.bundle.view.uimodel.CartItemHolderData
import com.tokopedia.cart.bundle.view.viewholder.now.CartCollapsedProductViewHolder

class CartCollapsedProductAdapter(val actionListener: ActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var cartCollapsedProductHolderDataList: List<CartItemHolderData> = arrayListOf()

    override fun getItemViewType(position: Int): Int {
        return CartCollapsedProductViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCartCollapsedProductBundleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartCollapsedProductViewHolder(binding, actionListener)
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