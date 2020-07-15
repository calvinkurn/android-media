package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus

interface CampaignStockListener {
    fun onTotalStockChanged(totalStock: Int)
    fun onActiveStockChanged(isActive: Boolean)

    fun onVariantStockChanged(productId: String, stock: Int)
    fun onVariantStatusChanged(productId: String, status: ProductStatus)
}