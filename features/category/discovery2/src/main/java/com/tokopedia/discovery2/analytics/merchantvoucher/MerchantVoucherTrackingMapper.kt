package com.tokopedia.discovery2.analytics.merchantvoucher

import com.tokopedia.design.viewpagerindicator.TitlePageIndicator.LinePosition
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem

object MerchantVoucherTrackingMapper {
    fun ComponentsItem.componentToMvcTrackingProperties(
        ctaText: String = "",
        creativeName: String,
        compId: String
    ): MvcTrackingProperties {
        val gtmItemName = data?.firstOrNull()?.gtmItemName.orEmpty()

        return MvcTrackingProperties(
            name = gtmItemName,
            shopId = data?.firstOrNull()?.shopInfo?.id.orEmpty(),
            creativeName = creativeName,
            action = ctaText,
            gtmItem = gtmItemName,
            tabName = tabName.orEmpty(),
            position = position,
            compId = compId
        )
    }

    fun DataItem.dataToMvcTrackingProperties(
        ctaText: String = "",
        creativeName: String,
        compId: String,
        position: Int
    ): MvcTrackingProperties {
        val gtmItemName = this.gtmItemName.orEmpty()

        return MvcTrackingProperties(
            name = gtmItemName,
            shopId = this.shopInfo?.id.orEmpty(),
            creativeName = creativeName,
            action = ctaText,
            gtmItem = gtmItemName,
            position = position,
            tabName = tabName.orEmpty(),
            compId = compId
        )
    }
}
