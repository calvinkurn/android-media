package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationConst.SOURCE_MODULE_ADS
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationConst.SOURCE_MODULE_HORIZONTAL_FORMAT
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationConst.SOURCE_MODULE_REC
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationConst.SOURCE_MODULE_VERTICAL_FORMAT

object AppLogRecommendationConst {
    const val SOURCE_MODULE_REC = "rec"
    const val SOURCE_MODULE_ADS = "ads"
    const val SOURCE_MODULE_VERTICAL_FORMAT = "%s_%s_outer_flow"
    const val SOURCE_MODULE_HORIZONTAL_FORMAT = "%s_%s_outer_%s_module"
}

object CardName {
    const val REC_GOODS_CARD = "rec_goods_card"
    const val AD_GOODS_CARD = "ad_goods_card"
    const val REC_CONTENT_CARD = "rec_%s_card"
    const val REC_VIDEO_CARD = "rec_video_card"
    const val AD_FEED_CARD = "ad_feed_card"
    const val MISSION_PAGE_CARD = "mission_%s_page_card"
    const val MISSION_PRODUCT_CARD = "mission_%s_product_card"
}

internal fun constructSourceModule(
    isAd: Boolean,
    moduleName: String,
    entranceForm: EntranceForm,
): String {
    val prefix = if (isAd) {
        SOURCE_MODULE_ADS
    } else {
        SOURCE_MODULE_REC
    }

    val pageName = AppLogAnalytics.getCurrentData(AppLogParam.PAGE_NAME)
    return when (entranceForm) {
        EntranceForm.PURE_GOODS_CARD,
        EntranceForm.CONTENT_GOODS_CARD,
        EntranceForm.DETAIL_GOODS_CARD -> SOURCE_MODULE_VERTICAL_FORMAT.format(prefix, pageName)
        EntranceForm.HORIZONTAL_GOODS_CARD,
        EntranceForm.MISSION_HORIZONTAL_GOODS_CARD -> SOURCE_MODULE_HORIZONTAL_FORMAT.format(prefix, pageName, moduleName)
        else -> SOURCE_MODULE_HORIZONTAL_FORMAT.format(prefix, pageName, moduleName)
    }
}

internal fun constructTrackId(
    cardId: String?,
    productId: String,
    requestId: String,
    position: Int,
    cardName: String,
): String {
    return when (cardName) {
        CardName.REC_GOODS_CARD,
        CardName.AD_GOODS_CARD,
        CardName.MISSION_PRODUCT_CARD -> "${requestId}_${productId}_${position.inc()}"
        CardName.REC_VIDEO_CARD -> "${requestId}_${cardId}_${productId}_${position.inc()}"
        CardName.REC_CONTENT_CARD,
        CardName.AD_FEED_CARD,
        CardName.MISSION_PAGE_CARD -> "${requestId}_${cardId}_${position.inc()}"
        else -> "${requestId}_${cardId}_${position.inc()}"
    }
}

internal fun String.zeroAsEmpty(): String {
    return this.takeIf { it != "0" }.orEmpty()
}

internal fun getCardName(
    cardName: String,
    isAd: Boolean,
): String {
    return if(cardName == CardName.REC_GOODS_CARD) {
        if(isAd) CardName.AD_GOODS_CARD
        else CardName.REC_GOODS_CARD
    } else cardName
}
