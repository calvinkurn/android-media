package com.tokopedia.tokopedianow.common.constant

import androidx.annotation.StringDef

/**
 * Type for static layout type.
 */
@Retention(AnnotationRetention.SOURCE)
@StringDef(
    TokoNowStaticLayoutType.PRODUCT_ADS_CAROUSEL,
    TokoNowStaticLayoutType.PRODUCT_CARD_ITEM
)
annotation class TokoNowStaticLayoutType {
    companion object {
        const val PRODUCT_ADS_CAROUSEL = "product_ads_carousel"
        const val PRODUCT_CARD_ITEM = "product_card_item"
    }
}
