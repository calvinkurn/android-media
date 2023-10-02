package com.tokopedia.shop.common.view.interfaces

import android.view.View
import com.tokopedia.shop.common.view.model.ShopPageColorSchema

interface InterfaceShopPageHeader {

    fun onBackPressed()
    fun isTabSelected(tabFragmentClass: Class<out Any>): Boolean
    fun refreshData()
    fun isNewlyBroadcastSaved(): Boolean?
    fun clearIsNewlyBroadcastSaved()
    fun collapseAppBar()
    fun onTabFragmentWrapperFinishLoad(){}
    fun getCartCounterData(): Int{ return 0}
    fun getBodyColorSchema(): ShopPageColorSchema?{ return null}
    fun isOverrideTheme(): Boolean = false
    fun getBodyPatternColorType(): String = ""
    fun getBodyBackgroundHexColor(): String = ""
    fun getBottomViewContainer(): View? = null
}
