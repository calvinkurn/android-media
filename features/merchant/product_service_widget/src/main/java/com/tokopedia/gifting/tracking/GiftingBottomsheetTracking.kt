package com.tokopedia.gifting.tracking

import android.content.Context
import com.tokopedia.gifting.domain.model.Addon

/**
 * Thanos link: https://mynakama.tokopedia.com/datatracker/requestdetail/view/2737
 */
object GiftingBottomsheetTracking: BaseGiftingTracking() {
    private const val EVENT_CATEGORY = "product detail page - user guideline bottomsheet"
    private const val EVENT_LABEL_FORMAT = "bottomsheet_title:%s;"
    private const val EVENT_ACTION_INFO_URL_CLICK = "click - info selengkapnya on informasi wilayah"
    private const val EVENT_ACTION_PAGE_IMPRESSION = "impression - produk pelengkap bingkisan"

    fun trackInfoURLClick(addonId: String, label: String, userId: String, shopIdDisplayed: String, shopTier: Long) {
        initializeTracker().sendClickEvent(
            addonId,
            EVENT_ACTION_INFO_URL_CLICK,
            EVENT_LABEL_FORMAT.format(label),
            EVENT_CATEGORY,
            userId,
            shopIdDisplayed,
            shopTier
        )
    }

    fun trackPageImpression(context: Context?, addonId: String, label: String, userId: String, shopIdDisplayed: String, shopTier: Long, addOnList: List<Addon>) {
        if (context == null) return
        initializeTracker().sendImpressionEvent(
            addonId,
            EVENT_ACTION_PAGE_IMPRESSION,
            EVENT_LABEL_FORMAT.format(label),
            EVENT_CATEGORY,
            addOnList.convertToPromotionData(context),
            userId,
            shopIdDisplayed,
            shopTier
        )
    }
}