package com.tokopedia.shop.flashsale.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.shop.flashsale.data.response.GetSellerCampaignListMetaResponse
import com.tokopedia.shop.flashsale.domain.entity.TabMeta
import javax.inject.Inject

class SellerCampaignListMetaMapper @Inject constructor() {

    fun map(data: GetSellerCampaignListMetaResponse): List<TabMeta> {
        return data.getSellerCampaignListMeta.tab.map {
            TabMeta(it.id.toIntOrZero(), it.totalCampaign, it.name, it.status)
        }
    }

}