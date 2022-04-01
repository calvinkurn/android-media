package com.tokopedia.shop.score.uitest.features.performance.activity

import androidx.test.espresso.matcher.ViewMatchers
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.SectionFaqUiModel
import com.tokopedia.shop.score.uitest.features.performance.base.ShopScoreUiTest
import com.tokopedia.shop.score.stub.common.util.isViewDisplayed
import com.tokopedia.shop.score.stub.common.util.onClick
import com.tokopedia.shop.score.stub.common.util.onIdView
import com.tokopedia.shop.score.stub.common.util.scrollTo
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.unifycomponents.ticker.Ticker
import org.hamcrest.CoreMatchers
import org.junit.Test

@UiTest
class ReactivatedBeforeMondayOSActivityUiTest: ShopScoreUiTest() {

    override fun setup() {
        super.setup()
        getShopInfoPeriodUseCaseStub.responseStub = shopInfoPeriodResponse
        getShopPerformanceUseCaseStub.responseStub = reactivatedBeforeMondayOsResponse
        shopScorePrefManagerStub.setFinishCoachMark(true)
    }

    @Test
    fun show_ticker_reactivated_before_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.tickerReactivatedSeller).isViewDisplayed()
        val tickerReactivatedBeforeMonday =
            activityRule.activity.findViewById<Ticker>(R.id.tickerReactivatedSeller)
        ViewMatchers.assertThat(
            tickerReactivatedBeforeMonday.tickerTitle,
            CoreMatchers.`is`(context.getString(R.string.title_ticker_reactivated_seller))
        )
    }

    @Test
    fun show_header_performance_when_reactivated_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        showHeaderPerformanceNewSellerOs(reactivatedBeforeMondayOsResponse, shopInfoPeriodAfterMondayResponse)
    }

    @Test
    fun show_bottom_sheet_tooltip_level_reactivated_before_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_performance_level_information).onClick()
        showBottomSheetTooltipLevelNew(reactivatedBeforeMondayOsResponse)
    }

    @Test
    fun show_section_period_detail_reactivated_before_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<PeriodDetailPerformanceUiModel>()
        showSectionPeriodDetailExisting(reactivatedBeforeMondayOsResponse)
    }

    @Test
    fun show_item_detail_performance_reactivated_before_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        showDetailPerformanceReactivatedBeforeMonday()
    }

    @Test
    fun show_faq_shop_score_new_reactivated_before_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<SectionFaqUiModel>()
        showFaqItemList()
    }
}