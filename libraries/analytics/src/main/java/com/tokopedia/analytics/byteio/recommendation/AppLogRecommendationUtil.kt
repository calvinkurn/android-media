package com.tokopedia.analytics.byteio.recommendation

import com.tokopedia.analytics.byteio.AppLogAnalytics
import com.tokopedia.analytics.byteio.AppLogParam
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationConst.SOURCE_MODULE_ADS
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationConst.SOURCE_MODULE_HORIZONTAL_FORMAT
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationConst.SOURCE_MODULE_OPS
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationConst.SOURCE_MODULE_REC
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendationConst.SOURCE_MODULE_VERTICAL_FORMAT

object AppLogRecommendationConst {
    const val SOURCE_MODULE_REC = "rec"
    const val SOURCE_MODULE_ADS = "ads"
    const val SOURCE_MODULE_OPS = "ops"
    const val SOURCE_MODULE_VERTICAL_FORMAT = "%s_%s_outer_flow"
    const val SOURCE_MODULE_HORIZONTAL_FORMAT = "%s_%s_outer_%s_module"
}

enum class CardName(val str: String) {
    REC_GOODS_CARD("rec_goods_card"),
    REC_CONTENT_CARD("rec_%s_card"),
    REC_VIDEO_CARD("rec_video_card"),
    AD_FEED_CARD("ad_feed_card"),
    MISSION_CARD("mission_%s_card")
}

fun constructSourceModule(
    isProduct: Boolean,
    isAd: Boolean,
    moduleName: String,
    entranceForm: EntranceForm,
): String {
    val prefix = if (isAd) {
        SOURCE_MODULE_ADS
    } else if (isProduct) {
        SOURCE_MODULE_REC
    } else {
        SOURCE_MODULE_OPS
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
