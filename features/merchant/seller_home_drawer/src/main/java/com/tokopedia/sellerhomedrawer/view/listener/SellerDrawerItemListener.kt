package com.tokopedia.sellerhomedrawer.view.listener

import com.tokopedia.sellerhomedrawer.view.viewmodel.SellerDrawerItem

interface SellerDrawerItemListener {

    fun onItemClicked(drawerItem: SellerDrawerItem)
    fun notifyDataSetChanged()

}