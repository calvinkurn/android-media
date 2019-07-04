package com.tokopedia.tkpd.home.wishlist.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.component.TextViewCompat
import com.tokopedia.tkpd.R
import com.tokopedia.tkpd.home.wishlist.adapter.viewmodel.WishlistRecomTitleViewModel

/**
 * Author errysuprayogi on 15,March,2019
 */
class WishlistRecomTitleViewHolder(itemView: View) : AbstractViewHolder<WishlistRecomTitleViewModel>(itemView) {

    private val textView: TextViewCompat

    init {
        textView = itemView.findViewById(R.id.title)
    }

    override fun bind(element: WishlistRecomTitleViewModel) {
        textView.text = element.title
    }

    companion object {

        @LayoutRes
        val LAYOUT = R.layout.wishlist_recom_title_item
    }
}
