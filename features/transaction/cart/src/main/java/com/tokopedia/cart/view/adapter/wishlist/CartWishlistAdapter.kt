package com.tokopedia.cart.view.adapter.wishlist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.cart.R
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.viewholder.CartWishlistItemViewHolder
import com.tokopedia.cart.view.uimodel.CartWishlistItemHolderData

/**
 * Created by Irfan Khoirul on 2019-06-15.
 */

class CartWishlistAdapter(val actionListener: ActionListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<CartWishlistItemHolderData> = arrayListOf()

    override fun getItemViewType(position: Int): Int {
        return CartWishlistItemViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(CartWishlistItemViewHolder.LAYOUT, parent, false)
        return CartWishlistItemViewHolder(view, actionListener, parent.context.resources.getDimension(R.dimen.dp_120).toInt())
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holderView = holder as CartWishlistItemViewHolder
        val data = list.get(position)
        holderView.bind(data)
    }

    fun updateWishlistItems(visitableList: List<CartWishlistItemHolderData>) {
        val diffResult = DiffUtil.calculateDiff(WishlistDiffUtilCallback(list, visitableList))

        list.clear()
        list.addAll(visitableList)

        diffResult.dispatchUpdatesTo(this)
    }

}