package com.tokopedia.shop.score.uitest.features.performance.activity

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.activity.ShopPerformanceYoutubeActivity
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.ProtectedParameterSectionUiModel
import com.tokopedia.shop.score.performance.presentation.model.SectionFaqUiModel
import com.tokopedia.shop.score.uitest.features.performance.base.ShopScoreUiTest
import com.tokopedia.shop.score.stub.common.util.isViewDisplayed
import com.tokopedia.shop.score.stub.common.util.onClick
import com.tokopedia.shop.score.stub.common.util.onIdView
import com.tokopedia.shop.score.stub.common.util.scrollTo
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class NewOsAfterMondayActivityUiTest: ShopScoreUiTest() {

    override fun setup() {
        super.setup()
        getShopInfoPeriodUseCaseStub.responseStub = shopInfoPeriodAfterMondayResponse
        getShopPerformanceUseCaseStub.responseStub = newOsAfterMondayResponse
        shopScorePrefManagerStub.setFinishCoachMark(true)
    }

    @Test
    fun show_timer_new_before_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        showTimerNewSellerAfterMonday()
    }

    @Test
    fun intended_youtube_page_click_btn_timer_new_before_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_video_shop_performance_learn).onClick()
        Intents.intended(IntentMatchers.hasComponent(ShopPerformanceYoutubeActivity::class.java.name))
    }

    @Test
    fun intended_webview_page_click_btn_timer_new_before_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.btn_shop_performance_learn).isViewDisplayed().onClick()
        intendingIntent()
    }

    @Test
    fun show_header_performance_when_new_after_monday_os() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        showHeaderPerformanceNewOsAfterMonday(newOsAfterMondayResponse, shopInfoPeriodAfterMondayResponse)
    }

    @Test
    fun show_bottom_sheet_tooltip_level_new_after_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_performance_level_information).onClick()
        showBottomSheetTooltipLevelOs(newOsAfterMondayResponse)
    }

    @Test
    fun show_bottom_sheet_tooltip_score_new_after_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_shop_score_performance).onClick()
        showBottomSheetTooltipScoreExisting()
    }

    @Test
    fun show_section_period_detail_new_after_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<PeriodDetailPerformanceUiModel>()
        showSectionPeriodDetailNew(newOsAfterMondayResponse)
    }

    @Test
    fun show_item_detail_performance_and_show_bottomsheet_new_after_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        showDetailPerformanceExisting()
    }

    @Test
    fun show_faq_shop_score_new_after_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<SectionFaqUiModel>()
        showFaqItemList()
    }

    @Test
    fun show_protected_parameter_new_after_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<ProtectedParameterSectionUiModel>()
        showProtectedParameterSection(newOsAfterMondayResponse)
    }
}