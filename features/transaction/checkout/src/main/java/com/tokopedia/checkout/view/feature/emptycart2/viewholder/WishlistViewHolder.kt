package com.tokopedia.checkout.view.feature.emptycart2.viewholder

import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.checkout.R
import com.tokopedia.checkout.view.feature.emptycart2.ActionListener
import com.tokopedia.checkout.view.feature.emptycart2.adapter.WishlistAdapter
import com.tokopedia.checkout.view.feature.emptycart2.uimodel.WishlistUiModel
import kotlinx.android.synthetic.main.item_checkout_product_wishlist.view.*

/**
 * Created by Irfan Khoirul on 2019-05-20.
 */

class WishlistViewHolder(val view: View, val listener: ActionListener, val itemWidth: Int) : AbstractViewHolder<WishlistUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_checkout_product_wishlist
    }

    override fun bind(element: WishlistUiModel) {
        itemView.tv_wish_list_see_all.setOnClickListener {
            listener.onShowAllWishlist();
        }
        setupRecyclerView(element)
    }

    private fun setupRecyclerView(element: WishlistUiModel) {
        val wishlistAdapter = WishlistAdapter(listener, itemWidth)
        wishlistAdapter.setData(element.wishlistItems)
        val gridLayoutManager = GridLayoutManager(itemView.context, 2)
        itemView.rv_wish_list.layoutManager = gridLayoutManager
        itemView.rv_wish_list.adapter = wishlistAdapter
        wishlistAdapter.notifyDataSetChanged()
    }

}