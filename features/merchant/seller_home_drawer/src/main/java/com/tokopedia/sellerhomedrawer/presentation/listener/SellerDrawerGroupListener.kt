package com.tokopedia.sellerhomedrawer.presentation.listener

import com.tokopedia.sellerhomedrawer.presentation.view.viewmodel.SellerDrawerGroup

interface SellerDrawerGroupListener {

    fun onGroupClicked(sellerDrawerGroup: SellerDrawerGroup, position: Int)
}