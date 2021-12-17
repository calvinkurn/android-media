package com.tokopedia.product.manage.common.feature.quickedit.common.interfaces

import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductCampaignType

interface ProductCampaignInfoListener {

    fun onClickCampaignInfo(campaignTypeList: List<ProductCampaignType>)

}