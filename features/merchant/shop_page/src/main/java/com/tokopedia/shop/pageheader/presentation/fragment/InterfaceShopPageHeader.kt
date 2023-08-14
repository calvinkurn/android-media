package com.tokopedia.shop.pageheader.presentation.fragment

interface InterfaceShopPageHeader {

    fun onBackPressed()
    fun isTabSelected(tabFragmentClass: Class<out Any>): Boolean
    fun refreshData()
    fun isNewlyBroadcastSaved(): Boolean?
    fun clearIsNewlyBroadcastSaved()
    fun collapseAppBar()
    fun onTabFragmentWrapperFinishLoad(){}
    fun getCartCounterData(): Int{ return 0}
    fun startDynamicUspCycle(){ return }
    fun getCurrentDynamicUspValue(): String{ return ""}
}
