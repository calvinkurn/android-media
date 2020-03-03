package com.tokopedia.shop.product.view.listener

import com.tokopedia.shop.analytic.ShopPageTrackingBuyer
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo

interface OnShopProductListFragmentListener {
    fun updateUIByShopName(shopName: String)
    fun updateUIByEtalaseName(etalaseName: String?)
}