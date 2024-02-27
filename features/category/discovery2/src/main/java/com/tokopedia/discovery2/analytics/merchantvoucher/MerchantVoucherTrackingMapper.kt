package com.tokopedia.discovery2.analytics.merchantvoucher

import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem

object MerchantVoucherTrackingMapper {
    fun ComponentsItem.componentToMvcTrackingProperties(
        ctaText: String = "",
    ): MvcTrackingProperties {
        val gtmItemName = data?.firstOrNull()?.gtmItemName.orEmpty()

        return MvcTrackingProperties(
            name = gtmItemName,
            shopId = data?.firstOrNull()?.shopInfo?.id.orEmpty(),
            creativeName = creativeName.orEmpty(),
            action = ctaText,
            gtmItem = gtmItemName,
            tabName = tabName.orEmpty(),
            position = position
        )
    }

    fun DataItem.dataToMvcTrackingProperties(
        ctaText: String = "",
        creativeName: String
    ): MvcTrackingProperties {
        val gtmItemName = this.gtmItemName.orEmpty()

        return MvcTrackingProperties(
            name = gtmItemName,
            shopId = this.shopInfo?.id.orEmpty(),
            creativeName = creativeName.orEmpty(),
            action = ctaText,
            gtmItem = gtmItemName,
            position = 0,
            tabName = tabName.orEmpty()
        )
    }
}
