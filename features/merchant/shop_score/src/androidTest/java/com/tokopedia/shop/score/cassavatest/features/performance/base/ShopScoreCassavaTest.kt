package com.tokopedia.shop.score.cassavatest.features.performance.base

import com.tokopedia.cassavatest.CassavaTestRule
import com.tokopedia.cassavatest.hasAllSuccess
import com.tokopedia.shop.score.common.BaseShopScoreTest
import com.tokopedia.shop.score.performance.presentation.model.SectionShopRecommendationUiModel
import com.tokopedia.shop.score.stub.common.util.scrollTo
import org.hamcrest.MatcherAssert
import org.junit.Rule

class ShopScoreCassavaTest : BaseShopScoreTest() {

    companion object {
        const val CLICK_HELP_CENTER_FAQ_NEW_SELLER_PATH =
            "tracker/merchant/shop_score/performance/sp_click_help_center_faq_new_seller.json"
        const val CLICK_LEARN_PERFORMANCE_NEW_SELLER_PATH =
            "tracker/merchant/shop_score/performance/sp_click_learn_performance_new_seller.json"
        const val CLICK_WATCH_VIDEO_NEW_SELLER_PATH =
            "tracker/merchant/shop_score/performance/sp_click_watch_video_new_seller.json"
        const val CLICK_MERCHANT_TOOLS_RECOMMENDATION_PATH =
            "tracker/merchant/sp_click_merchant_tools_recommandation.json"
        const val CLICK_POWER_MERCHANT_SECTION_PATH =
            "tracker/merchant/sp_click_power_merchant_section.json"
        const val CLICK_REGULAR_MERCHANT_SECTION_PATH =
            "tracker/merchant/sp_click_regular_merchant_section.json"
        const val CLICK_TICKER_PENALTY_PATH =
            "tracker/merchant/sp_click_ticker_penalty.json"
    }

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    protected fun validate(fileName: String) {
        MatcherAssert.assertThat(cassavaTestRule.validate(fileName), hasAllSuccess())
    }

    protected fun clickMerchantToolsRecommendation() {
        activityRule.activity.scrollTo<SectionShopRecommendationUiModel>()
    }
}