package com.tokopedia.shop.score.cassavatest.base

import androidx.test.espresso.contrib.RecyclerViewActions
import com.tokopedia.analyticsdebugger.cassava.cassavatest.CassavaTestRule
import com.tokopedia.analyticsdebugger.cassava.cassavatest.hasAllSuccess
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.common.BaseShopScoreTest
import com.tokopedia.shop.score.performance.presentation.adapter.viewholder.SectionShopFeatureRecommendationViewHolder
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusPMUiModel
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.SectionFaqUiModel
import com.tokopedia.shop.score.performance.presentation.model.SectionShopRecommendationUiModel
import com.tokopedia.shop.score.stub.common.util.clickClickableSpan
import com.tokopedia.shop.score.stub.common.util.getHyperlinkText
import com.tokopedia.shop.score.stub.common.util.isViewDisplayed
import com.tokopedia.shop.score.stub.common.util.onClick
import com.tokopedia.shop.score.stub.common.util.onIdView
import com.tokopedia.shop.score.stub.common.util.scrollTo
import com.tokopedia.test.application.espresso_component.CommonActions
import org.hamcrest.MatcherAssert
import org.junit.Rule

open class ShopScoreCassavaTest : BaseShopScoreTest() {

    companion object {
        const val CLICK_HELP_CENTER_FAQ_NEW_SELLER_PATH =
            "tracker/merchant/shop_score/performance/sp_click_help_center_faq_new_seller.json"
        const val CLICK_LEARN_PERFORMANCE_NEW_SELLER_PATH =
            "tracker/merchant/shop_score/performance/sp_click_learn_performance_new_seller.json"
        const val CLICK_WATCH_VIDEO_NEW_SELLER_PATH =
            "tracker/merchant/shop_score/performance/sp_click_watch_video_new_seller.json"
        const val CLICK_MERCHANT_TOOLS_RECOMMENDATION_PATH =
            "tracker/merchant/shop_score/performance/sp_click_merchant_tools_recommandation.json"
        const val CLICK_POWER_MERCHANT_SECTION_PATH =
            "tracker/merchant/shop_score/performance/sp_click_power_merchant_section.json"
        const val CLICK_TICKER_PENALTY_PATH =
            "tracker/merchant/shop_score/performance/sp_click_ticker_penalty.json"
    }

    @get:Rule
    var cassavaTestRule = CassavaTestRule()

    protected fun validate(fileName: String) {
        activityRule.finishActivity()
        Thread.sleep(3000)
        MatcherAssert.assertThat(cassavaTestRule.validate(fileName), hasAllSuccess())
    }

    protected fun clickMerchantToolsRecommendation() {
        activityRule.activity.scrollTo<SectionShopRecommendationUiModel>()
        onIdView(R.id.rvShopScoreCreation).isViewDisplayed()
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<SectionShopFeatureRecommendationViewHolder>(
                    0,
                    CommonActions.clickChildViewWithId(R.id.cardPromoCreation)
                )
            )
        validate(CLICK_MERCHANT_TOOLS_RECOMMENDATION_PATH)
    }

    protected fun clickPowerMerchantSection() {
        activityRule.activity.scrollTo<ItemStatusPMUiModel>()
        onIdView(R.id.potentialPowerMerchantWidget).isViewDisplayed().onClick()
        validate(CLICK_POWER_MERCHANT_SECTION_PATH)
    }

    protected fun clickTickerPenalty() {
        activityRule.activity.scrollTo<PeriodDetailPerformanceUiModel>()
        onIdView(com.tokopedia.unifycomponents.R.id.ticker_description).isViewDisplayed().onClick()
        validate(CLICK_TICKER_PENALTY_PATH)
    }

    protected fun clickWatchVideoNewSeller() {
        onIdView(R.id.ic_video_shop_performance_learn).isViewDisplayed().onClick()
        validate(CLICK_WATCH_VIDEO_NEW_SELLER_PATH)
    }

    protected fun clickLearnPerformanceNewSeller() {
        onIdView(R.id.btn_shop_performance_learn).isViewDisplayed().onClick()
        validate(CLICK_LEARN_PERFORMANCE_NEW_SELLER_PATH)
    }

    protected fun clickHelpCenterFaqNewSeller() {
        activityRule.activity.scrollTo<SectionFaqUiModel>()
        onIdView(
            R.id.tv_label_help_center
        ).isViewDisplayed().perform(
            clickClickableSpan(
                getHyperlinkText(
                    context,
                    context.getString(R.string.title_help_center_tokopedia)
                )
            )
        )
        validate(CLICK_HELP_CENTER_FAQ_NEW_SELLER_PATH)
    }
}