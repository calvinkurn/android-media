package com.tokopedia.shop_showcase.common

import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseItem

interface ShopShowcaseListener {

}

interface ShopShowcaseManagementListener {
    fun sendClickShowcaseMenuMore(dataShowcase: ShowcaseItem, position: Int)
    fun sendClickShowcase(dataShowcase: ShowcaseItem, position: Int)
    fun onSuccessUpdateShowcase()
}

interface ShopShowcaseReorderListener {

}

interface ShopShowcaseFragmentNavigation {
    fun navigateToPage(page: String, tag: String?, showcaseList: ArrayList<ShowcaseItem>?)
}