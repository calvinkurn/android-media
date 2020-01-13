package com.tokopedia.sellerhomedrawer.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.view.listener.SellerDrawerItemListener
import com.tokopedia.sellerhomedrawer.view.viewmodel.SellerDrawerSeparator

class SellerDrawerSeparatorViewHolder(itemView: View,
                                      val listener: SellerDrawerItemListener)
    : AbstractViewHolder<SellerDrawerSeparator>(itemView) {

    companion object {
        val LAYOUT_RES = R.layout.seller_drawer_child_separator

    }

    override fun bind(element: SellerDrawerSeparator?) {

    }
}