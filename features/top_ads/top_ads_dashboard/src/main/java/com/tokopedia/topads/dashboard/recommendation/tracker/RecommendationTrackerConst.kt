package com.tokopedia.topads.dashboard.recommendation.tracker

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_GROUP_LIST
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_GROUP_LIST_HOMEPAGE
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_LIHAT_SELENGKAPNYA_IKLAN_PRODUK
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_LIHAT_SELENGKAPNYA_IKLAN_TOKO
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_LIHAT_SELENGKAPNYA_SARAN_TOPADS
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_LIHAT_SELENGKAPNYA_SARAN_TOPADS_HEADLINE
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_PRODUCT_OOS
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_PRODUCT_RECOMMENDATION
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SELENGKAPNYA_HOMEPAGE
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SUBMIT_BID_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SUBMIT_DAILY_BUDGET
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SUBMIT_GROUP_BID
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SUBMIT_NEGATIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SUBMIT_POSITIVE_KEYWORD
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_SUBMIT_PRODUCT_RECOMMENDATION
import com.tokopedia.topads.dashboard.recommendation.tracker.RecommendationTrackerConst.Action.CLICK_TAB_SARAN_TOPADS

object RecommendationTrackerConst {
    const val EVENT = "clickTopAds"
    const val EVENT_CATEGORY_HOMEPAGE = "topads homepage dashboard"
    const val EVENT_CATEGORY_INSIGHT_CENTRE = "insight center"
    const val EVENT_CATEGORY_MANUAL_ADS_DASHBOARD = "manual ads dashboard"
    const val EVENT_CATEGORY_TOPADS_DETAIL_GROUP_IKLAN = "topads detail group iklan"
    const val EVENT_CATEGORY_TOPADS_DASHBOARD_HEADLINE = "topads dashboard headline"
    const val EVENT_LABEL_EXISTING_GROUP = "existing group"
    const val EVENT_LABEL_NEW_GROUP = "new group"
    const val BUSINESS_UNIT = "ads solution"
    const val CURRENT_SITE = "tokopediamarketplace"

    object Action {
        const val CLICK_GROUP_LIST_HOMEPAGE = "click - group list homepage"
        const val CLICK_SELENGKAPNYA_HOMEPAGE = "click - lihat selengkapnya homepage"
        const val CLICK_PRODUCT_RECOMMENDATION = "click - product recommendation insight"
        const val CLICK_PRODUCT_OOS = "click - product oos insight"
        const val CLICK_GROUP_LIST = "click - group list insight"
        const val CLICK_SUBMIT_BID_KEYWORD = "click - submit bid keyword insight"
        const val CLICK_SUBMIT_DAILY_BUDGET = "click - submit daily budget insight"
        const val CLICK_SUBMIT_GROUP_BID = "click - submit group bid insight"
        const val CLICK_SUBMIT_NEGATIVE_KEYWORD = "click - submit negative keyword insight"
        const val CLICK_SUBMIT_POSITIVE_KEYWORD = "click - submit positive keyword insight"
        const val CLICK_SUBMIT_PRODUCT_RECOMMENDATION = "click - submit product recommendation"
        const val CLICK_TAB_SARAN_TOPADS = "click - tab saran topads"
        const val CLICK_LIHAT_SELENGKAPNYA_SARAN_TOPADS = "click - lihat selengkapnya saran topads"
        const val CLICK_LIHAT_SELENGKAPNYA_IKLAN_PRODUK = "click - lihat selengkapnya saran topads iklan produk"
        const val CLICK_LIHAT_SELENGKAPNYA_IKLAN_TOKO = "click - lihat selengkapnya saran topads iklan toko"
        const val CLICK_LIHAT_SELENGKAPNYA_SARAN_TOPADS_HEADLINE = "click - lihat selengkapnya saran topads"
    }

    private val trackerIds = mapOf(
        CLICK_GROUP_LIST_HOMEPAGE to "47470",
        CLICK_SELENGKAPNYA_HOMEPAGE to "47471",
        CLICK_PRODUCT_RECOMMENDATION to "47472",
        CLICK_PRODUCT_OOS to "47473",
        CLICK_GROUP_LIST to "47474",
        CLICK_SUBMIT_BID_KEYWORD to "47475",
        CLICK_SUBMIT_DAILY_BUDGET to "47476",
        CLICK_SUBMIT_GROUP_BID to "47477",
        CLICK_SUBMIT_NEGATIVE_KEYWORD to "47479",
        CLICK_SUBMIT_POSITIVE_KEYWORD to "47480",
        CLICK_SUBMIT_PRODUCT_RECOMMENDATION to "47481",
        CLICK_TAB_SARAN_TOPADS to "47674",
        CLICK_LIHAT_SELENGKAPNYA_SARAN_TOPADS to "47675",
        CLICK_LIHAT_SELENGKAPNYA_IKLAN_PRODUK to "47676",
        CLICK_LIHAT_SELENGKAPNYA_SARAN_TOPADS_HEADLINE to "47677",
        CLICK_LIHAT_SELENGKAPNYA_IKLAN_TOKO to "47678",
        )

    fun getTrackerId(action: String): String {
        return trackerIds[action] ?: String.EMPTY
    }
}
