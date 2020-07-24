package com.tokopedia.shop_showcase.shop_showcase_add.presentation.listener

import com.tokopedia.shop_showcase.shop_showcase_product_add.presentation.model.ShowcaseProduct

interface ShopShowcasePreviewListener {

    fun deleteSelectedProduct(position: Int)
    fun showChooseProduct()
    fun setupDeleteCounter(firstDeletedItem: ShowcaseProduct)
    fun showDeleteCounter()
    fun hideDeleteCounter()

}