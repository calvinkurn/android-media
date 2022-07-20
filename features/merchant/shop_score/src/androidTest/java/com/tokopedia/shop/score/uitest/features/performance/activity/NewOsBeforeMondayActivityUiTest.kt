package com.tokopedia.shop.score.uitest.features.performance.activity

import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.activity.ShopPerformanceYoutubeActivity
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.SectionFaqUiModel
import com.tokopedia.shop.score.uitest.features.performance.base.ShopScoreUiTest
import com.tokopedia.shop.score.stub.common.util.onClick
import com.tokopedia.shop.score.stub.common.util.onIdView
import com.tokopedia.shop.score.stub.common.util.scrollTo
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class NewOsBeforeMondayActivityUiTest: ShopScoreUiTest() {

    override fun setup() {
        super.setup()
        getShopInfoPeriodUseCaseStub.responseStub = shopInfoPeriodAfterMondayResponse
        getShopPerformanceUseCaseStub.responseStub = newOsBeforeMondayResponse
        shopScorePrefManagerStub.setFinishCoachMark(true)
    }

    @Test
    fun show_timer_new_os_before_monday() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        showTimerNewSeller()
    }

    @Test
    fun intended_youtube_page_click_btn_timer_new_os_before_monday() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_video_shop_performance_learn).onClick()
        intended(hasComponent(ShopPerformanceYoutubeActivity::class.java.name))
    }

    @Test
    fun show_header_performance_when_new_os_before_monday() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        showHeaderPerformanceNewSellerOs(newOsBeforeMondayResponse, shopInfoPeriodAfterMondayResponse)
    }

    @Test
    fun show_bottom_sheet_tooltip_level_new_os_before_monday() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_performance_level_information).onClick()
        showBottomSheetTooltipLevelNew(newOsBeforeMondayResponse)
    }

    @Test
    fun show_section_period_detail_new_os_before_monday() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<PeriodDetailPerformanceUiModel>()
        showSectionPeriodDetailNew(newOsBeforeMondayResponse)
    }

    @Test
    fun show_item_detail_performance_new_os_before_monday() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        showDetailPerformanceNewOsBeforeMonday()
    }

    @Test
    fun show_faq_shop_score_new_os_before_monday() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<SectionFaqUiModel>()
        showFaqItemList()
    }
}