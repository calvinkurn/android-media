package com.tokopedia.tkpd.flashsale.data.mapper

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tkpd.flashsale.data.response.GetFlashSaleListForSellerMetaResponse
import com.tokopedia.tkpd.flashsale.domain.entity.TabMetadata
import javax.inject.Inject

class GetFlashSaleListForSellerMetaMapper @Inject constructor() {

    fun map(response: GetFlashSaleListForSellerMetaResponse): TabMetadata {
        val tabs = response.getFlashSaleListForSellerMeta.tabList.map { tab ->
            TabMetadata.Tab(
                tab.tabId.toIntOrZero(),
                tab.tabName,
                tab.totalCampaign,
                tab.displayName
            )
        }
        return TabMetadata(tabs)
    }

}
