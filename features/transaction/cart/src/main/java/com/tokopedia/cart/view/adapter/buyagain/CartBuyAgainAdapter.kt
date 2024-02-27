package com.tokopedia.cart.view.adapter.buyagain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.databinding.ItemCartBuyAgainViewAllBinding
import com.tokopedia.cart.databinding.ItemProductBuyAgainBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartBuyAgainItem
import com.tokopedia.cart.view.uimodel.CartBuyAgainItemHolderData
import com.tokopedia.cart.view.uimodel.CartBuyAgainViewAllData
import com.tokopedia.cart.view.viewholder.CartBuyAgainItemViewHolder
import com.tokopedia.cart.view.viewholder.CartBuyAgainViewAllViewHolder
import com.tokopedia.cart.view.viewholder.CartRecentViewItemViewHolder

class CartBuyAgainAdapter(val actionListener: ActionListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var buyAgainList: List<CartBuyAgainItem> = arrayListOf()

    override fun getItemViewType(position: Int): Int {
        return when (buyAgainList[position]) {
            is CartBuyAgainItemHolderData -> CartBuyAgainItemViewHolder.LAYOUT
            is CartBuyAgainViewAllData -> CartBuyAgainViewAllViewHolder.LAYOUT
            else -> super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            CartBuyAgainItemViewHolder.LAYOUT -> {
                val binding = ItemProductBuyAgainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CartBuyAgainItemViewHolder(binding, actionListener)
            }
            CartBuyAgainViewAllViewHolder.LAYOUT -> {
                val binding = ItemCartBuyAgainViewAllBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CartBuyAgainViewAllViewHolder(binding, actionListener)
            }
            else -> throw RuntimeException("No view holder type found")
        }
    }

    override fun getItemCount(): Int {
        return buyAgainList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val data = buyAgainList[position]) {
            is CartBuyAgainItemHolderData -> {
                val holderView = holder as CartBuyAgainItemViewHolder
                holderView.bind(data)
            }
            is CartBuyAgainViewAllData -> {
                val holderView = holder as CartBuyAgainViewAllViewHolder
                holderView.bind(data)
            }
        }
    }
}
