package com.tokopedia.product.detail.data.model.datamodel

import com.tokopedia.product.detail.common.data.model.pdplayout.DynamicProductInfoP1

data class ProductDetailDataModel(
        val layoutData: DynamicProductInfoP1 = DynamicProductInfoP1(),
        val listOfLayout: MutableList<DynamicPdpDataModel> = mutableListOf()
)