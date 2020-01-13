package com.tokopedia.sellerhomedrawer.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomedrawer.R
import com.tokopedia.sellerhomedrawer.view.listener.SellerDrawerItemListener
import com.tokopedia.sellerhomedrawer.view.viewmodel.SellerDrawerGroup

class SellerDrawerGroupViewHolder(itemView: View,
                                  val listener: SellerDrawerItemListener)
    : AbstractViewHolder<SellerDrawerGroup>(itemView) {

    companion object {
        val LAYOUT_RES = R.layout.seller_drawer_group
    }

    override fun bind(element: SellerDrawerGroup?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}