package com.tokopedia.purchase_platform.features.cart.view.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.cart.view.ActionListener
import com.tokopedia.purchase_platform.features.cart.view.viewholder.CartRecentViewItemViewHolder
import com.tokopedia.purchase_platform.features.cart.view.viewmodel.CartRecentViewItemHolderData

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartRecentViewAdapter(val actionListener: ActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var recentViewItemHoldeDataList: List<CartRecentViewItemHolderData> = arrayListOf()

    override fun getItemViewType(position: Int): Int {
        return CartRecentViewItemViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(CartRecentViewItemViewHolder.LAYOUT, parent, false)
        return CartRecentViewItemViewHolder(view, actionListener, parent.context.resources.getDimension(R.dimen.dp_120).toInt())
    }

    override fun getItemCount(): Int {
        return recentViewItemHoldeDataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderView = holder as CartRecentViewItemViewHolder
        val data = recentViewItemHoldeDataList.get(position)
        holderView.bind(data)
    }

}