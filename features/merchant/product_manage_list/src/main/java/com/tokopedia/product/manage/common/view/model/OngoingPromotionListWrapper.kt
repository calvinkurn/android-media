package com.tokopedia.product.manage.common.view.model

import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductCampaignType

data class OngoingPromotionListWrapper(
    val ongoingPromotionList: ArrayList<ProductCampaignType> = arrayListOf()
)