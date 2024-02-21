package com.tokopedia.cart.view.adapter.buyagain

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.databinding.ItemProductBuyAgainBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartBuyAgainItemHolderData
import com.tokopedia.cart.view.viewholder.CartBuyAgainItemViewHolder
import com.tokopedia.cart.view.viewholder.CartRecentViewItemViewHolder

class CartBuyAgainAdapter(val actionListener: ActionListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var buyAgainList: List<CartBuyAgainItemHolderData> = arrayListOf()

    override fun getItemViewType(position: Int): Int {
        return CartRecentViewItemViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemProductBuyAgainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartBuyAgainItemViewHolder(binding, actionListener)
    }

    override fun getItemCount(): Int {
        return buyAgainList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderView = holder as CartBuyAgainItemViewHolder
        val data = buyAgainList[position]
        holderView.bind(data)
    }
}
