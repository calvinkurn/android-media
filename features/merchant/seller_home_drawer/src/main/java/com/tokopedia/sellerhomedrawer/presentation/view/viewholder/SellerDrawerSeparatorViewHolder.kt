package com.tokopedia.sellerhomedrawer.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerSeparator

class SellerDrawerSeparatorViewHolder(itemView: View)
    : AbstractViewHolder<SellerDrawerSeparator>(itemView) {

    companion object {
        val LAYOUT_RES = R.layout.sh_drawer_child_separator

    }

    override fun bind(element: SellerDrawerSeparator?) {

    }
}