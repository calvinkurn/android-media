package com.tokopedia.shop_showcase.common

import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseItem



interface ShopShowcaseManagementListener {
    fun sendClickShowcaseMenuMore(dataShowcase: ShopEtalaseModel, position: Int)
    fun sendClickShowcase(dataShowcase: ShopEtalaseModel, position: Int)
    fun onSuccessUpdateShowcase()
}

interface ShopShowcaseReorderListener {

}

interface ShopShowcaseFragmentNavigation {
    fun navigateToPage(page: String, tag: String?, showcaseList: ArrayList<ShopEtalaseModel>?)
}