package com.tokopedia.sellerhomedrawer.presentation.listener

import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerItem

interface SellerDrawerItemListener {

    fun onItemClicked(drawerItem: SellerDrawerItem)
    fun notifyDataSetChanged()

}