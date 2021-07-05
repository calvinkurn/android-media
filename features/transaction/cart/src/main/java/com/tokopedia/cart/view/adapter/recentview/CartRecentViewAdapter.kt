package com.tokopedia.cart.view.adapter.recentview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cart.databinding.ItemProductRecentViewBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartRecentViewItemHolderData
import com.tokopedia.cart.view.viewholder.CartRecentViewItemViewHolder

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartRecentViewAdapter(val actionListener: ActionListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var recentViewItemHoldeDataList: List<CartRecentViewItemHolderData> = arrayListOf()

    override fun getItemViewType(position: Int): Int {
        return CartRecentViewItemViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemProductRecentViewBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return CartRecentViewItemViewHolder(binding, actionListener)
    }

    override fun getItemCount(): Int {
        return recentViewItemHoldeDataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderView = holder as CartRecentViewItemViewHolder
        val data = recentViewItemHoldeDataList[position]
        holderView.bind(data)
    }

}