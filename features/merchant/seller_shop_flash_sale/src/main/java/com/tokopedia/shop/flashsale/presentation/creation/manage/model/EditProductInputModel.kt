package com.tokopedia.shop.flashsale.presentation.creation.manage.model

import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList

data class EditProductInputModel (
    var productId: String = "",
    var stock: Long? = null,
    var maxOrder: Int? = null,
    var price: Long? = null,
    var warehouseId: String = "",
    var originalStock: Long = 0,
    var productMapData: SellerCampaignProductList.ProductMapData = SellerCampaignProductList.ProductMapData()
)