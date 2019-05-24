package com.tokopedia.checkout.view.feature.emptycart2.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.checkout.view.feature.emptycart2.ActionListener
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.WishlistItemUiModel
import com.tokopedia.checkout.view.feature.emptycart2.viewholder.WishlistItemViewHolder
import com.tokopedia.wishlist.common.data.source.cloud.model.Wishlist

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class WishlistAdapter(val listener: ActionListener, val itemWidth: Int) : RecyclerView.Adapter<WishlistItemViewHolder>() {

    private var wishlistUiModels = ArrayList<WishlistItemUiModel>()

    fun setData(wishlistItems: List<Wishlist>) {
        for (wishlist: Wishlist in wishlistItems) {
            val wishlistUiModel = WishlistItemUiModel()
            wishlistUiModel.wishlist = wishlist
            wishlistUiModels.add(wishlistUiModel)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(WishlistItemViewHolder.LAYOUT, parent, false)
        return WishlistItemViewHolder(view, listener, itemWidth)
    }

    override fun getItemCount(): Int {
        return wishlistUiModels.size
    }

    override fun onBindViewHolder(holder: WishlistItemViewHolder, position: Int) {
        holder.bind(wishlistUiModels.get(position))
    }

}