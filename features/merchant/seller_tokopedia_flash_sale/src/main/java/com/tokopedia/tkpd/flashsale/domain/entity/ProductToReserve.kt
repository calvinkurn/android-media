package com.tokopedia.tkpd.flashsale.domain.entity

import com.tokopedia.campaign.entity.ChooseProductItem

data class ProductToReserve(
    val selectedProductIds: List<Long>,
    val selectedProductCount: Int,
    val productList: List<ChooseProductItem>
)
