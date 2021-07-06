package com.tokopedia.cart.view.adapter.collapsedproduct

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.databinding.ItemCartCollapsedProductBinding
import com.tokopedia.cart.view.uimodel.new.CartCollapsedProductHolderData
import com.tokopedia.cart.view.viewholder.new.CartCollapsedProductViewHolder

class CartCollapsedProductAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var cartCollapsedProductHolderDataList: List<CartCollapsedProductHolderData> = arrayListOf()

    override fun getItemViewType(position: Int): Int {
        return CartCollapsedProductViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemCartCollapsedProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartCollapsedProductViewHolder(binding)
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