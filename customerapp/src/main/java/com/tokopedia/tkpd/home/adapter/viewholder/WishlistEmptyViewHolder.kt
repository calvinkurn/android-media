package com.tokopedia.tkpd.home.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import android.widget.Button

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tkpd.R
import com.tokopedia.tkpd.home.adapter.OnWishlistActionButtonClicked
import com.tokopedia.tkpd.home.adapter.viewmodel.WishlistEmptyViewModel

/**
 * Author errysuprayogi on 03,July,2019
 */
class WishlistEmptyViewHolder(itemView: View, actionButtonClicked: OnWishlistActionButtonClicked) : AbstractViewHolder<WishlistEmptyViewModel>(itemView) {

    override fun bind(element: WishlistEmptyViewModel) {
        itemView.findViewById<Button>(R.id.action_btn).setOnClickListener {

        }
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.layout_wishlist_empty_state
    }
}
