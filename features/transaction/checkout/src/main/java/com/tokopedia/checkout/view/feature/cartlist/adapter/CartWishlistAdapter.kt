package com.tokopedia.checkout.view.feature.cartlist.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.cartlist.ActionListener
import com.tokopedia.checkout.view.feature.cartlist.viewholder.CartWishlistItemViewHolder
import com.tokopedia.checkout.view.feature.cartlist.viewmodel.CartWishlistItemHolderData

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartWishlistAdapter(val actionListener: ActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var wishlistItemHoldeDataList: List<CartWishlistItemHolderData> = arrayListOf()

    override fun getItemViewType(position: Int): Int {
        return CartWishlistItemViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(CartWishlistItemViewHolder.LAYOUT, parent, false)
        return CartWishlistItemViewHolder(view, actionListener, parent.context.resources.getDimension(R.dimen.dp_120).toInt())
    }

    override fun getItemCount(): Int {
        return wishlistItemHoldeDataList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderView = holder as CartWishlistItemViewHolder
        val data = wishlistItemHoldeDataList.get(position)
        holderView.bind(data)
    }

}