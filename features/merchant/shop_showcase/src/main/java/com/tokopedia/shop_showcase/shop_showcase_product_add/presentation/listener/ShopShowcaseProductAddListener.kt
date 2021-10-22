package com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.listener

import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct

interface ShopShowcaseProductAddListener {
    fun onCLickProductCardTracking()
    fun showProductCounter(totalSelectedProduct: Int, excludedProduct: ArrayList<ShowcaseProduct>, selectedProduct: ArrayList<ShowcaseProduct>)
}