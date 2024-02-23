package com.tokopedia.analytics.byteio.recommendation

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

enum class AppLogRecommendationType {
    VERTICAL,
    PRODUCT_CAROUSEL,
    MIXED_CAROUSEL,
    SINGLE_PRODUCT
}

fun constructSourceModule(
    isProduct: Boolean,
    isAd: Boolean,
    moduleName: String,
    type: AppLogRecommendationType
): String {
    val prefix = if (isAd) {
        SOURCE_MODULE_ADS
    } else if (isProduct) {
        SOURCE_MODULE_REC
    } else {
        SOURCE_MODULE_OPS
    }

    val pageName = "pagename" // TODO get from global param
    return if (type == AppLogRecommendationType.VERTICAL) {
        SOURCE_MODULE_VERTICAL_FORMAT.format(prefix, pageName)
    } else {
        SOURCE_MODULE_HORIZONTAL_FORMAT.format(prefix, pageName, moduleName)
    }
}
