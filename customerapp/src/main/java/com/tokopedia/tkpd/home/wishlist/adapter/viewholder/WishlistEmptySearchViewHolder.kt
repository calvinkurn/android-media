package com.tokopedia.tkpd.home.wishlist.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Button

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tkpd.R
import com.tokopedia.tkpd.home.adapter.OnWishlistActionButtonClicked
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistEmptySearchViewModel

/**
 * Author errysuprayogi on 03,July,2019
 */
class WishlistEmptySearchViewHolder(itemView: View, var actionButtonClicked: OnWishlistActionButtonClicked) : AbstractViewHolder<WishlistEmptySearchViewModel>(itemView) {

    override fun bind(element: WishlistEmptySearchViewModel) {
        itemView.findViewById<Button>(R.id.action_btn).setOnClickListener {
            actionButtonClicked.showAllWishlist()
        }
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.layout_wishlist_empty_search
    }
}
