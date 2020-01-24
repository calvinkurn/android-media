package com.tokopedia.sellerhomedrawer.presentation.view.adapter

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomedrawer.presentation.listener.*
import com.tokopedia.sellerhomedrawer.presentation.view.viewholder.*
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerGroup
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerItem
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerSeparator
import com.tokopedia.sellerhomedrawer.data.header.DrawerHeader
import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.sellerheader.SellerDrawerHeader

class SellerDrawerAdapterTypeFactory(val sellerDrawerItemListener: SellerDrawerItemListener,
                                     val sellerDrawerHeaderListener: SellerDrawerHeaderListener,
                                     val sellerDrawerGroupListener: SellerDrawerGroupListener,
                                     val drawerHeaderListener: DrawerHeaderListener,
                                     val retryTokoCashListener: RetryTokoCashListener,
                                     val context: Context)
    : BaseAdapterTypeFactory(), AdapterTypeFactory {

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when(type) {
            SellerDrawerItemViewHolder.LAYOUT_RES ->
                SellerDrawerItemViewHolder(parent, sellerDrawerItemListener)
            SellerDrawerGroupViewHolder.LAYOUT_RES ->
                SellerDrawerGroupViewHolder(parent, sellerDrawerGroupListener)
            SellerDrawerSeparatorViewHolder.LAYOUT_RES ->
                SellerDrawerSeparatorViewHolder(parent)
            DrawerHeaderViewHolder.LAYOUT_RES ->
                DrawerHeaderViewHolder(parent, drawerHeaderListener, retryTokoCashListener, context)
            SellerDrawerHeaderViewHolder.LAYOUT_RES ->
                SellerDrawerHeaderViewHolder(parent, sellerDrawerHeaderListener, context)
            else -> super.createViewHolder(parent, type)
        }
    }

    fun type(sellerDrawerHeader: SellerDrawerHeader): Int {
        return SellerDrawerHeaderViewHolder.LAYOUT_RES
    }

    fun type(drawerHeader: DrawerHeader): Int {
         return DrawerHeaderViewHolder.LAYOUT_RES
    }

    fun type(sellerDrawerItem: SellerDrawerItem): Int {
        return SellerDrawerItemViewHolder.LAYOUT_RES
    }

    fun type(sellerDrawerGroup: SellerDrawerGroup): Int {
        return SellerDrawerGroupViewHolder.LAYOUT_RES
    }

    fun type(sellerDrawerSeparator: SellerDrawerSeparator): Int {
        return SellerDrawerSeparatorViewHolder.LAYOUT_RES
    }


}