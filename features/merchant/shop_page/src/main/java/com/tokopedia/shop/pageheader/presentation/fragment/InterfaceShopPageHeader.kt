package com.tokopedia.shop.pageheader.presentation.fragment

interface InterfaceShopPageHeader {

    fun onBackPressed()
    fun isTabSelected(tabFragmentClass: Class<out Any>): Boolean
    fun refreshData()
    fun isNewlyBroadcastSaved(): Boolean?
    fun clearIsNewlyBroadcastSaved()
    fun collapseAppBar()
}