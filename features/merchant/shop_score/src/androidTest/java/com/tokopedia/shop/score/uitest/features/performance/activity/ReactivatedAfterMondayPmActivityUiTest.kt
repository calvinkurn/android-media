package com.tokopedia.shop.score.uitest.features.performance.activity

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.activity.ShopPerformanceYoutubeActivity
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusPMUiModel
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.ProtectedParameterSectionUiModel
import com.tokopedia.shop.score.uitest.features.performance.base.ShopScoreUiTest
import com.tokopedia.shop.score.stub.common.util.isViewDisplayed
import com.tokopedia.shop.score.stub.common.util.onClick
import com.tokopedia.shop.score.stub.common.util.onIdView
import com.tokopedia.shop.score.stub.common.util.scrollTo
import com.tokopedia.shop.score.stub.common.util.withTextStr
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class ReactivatedAfterMondayPmActivityUiTest: ShopScoreUiTest() {

    override fun setup() {
        super.setup()
        getShopInfoPeriodUseCaseStub.responseStub = shopInfoPeriodResponse
        getShopPerformanceUseCaseStub.responseStub = reactivatedAfterMondayPmResponse
        shopScorePrefManagerStub.setFinishCoachMark(true)
    }

    @Test
    fun show_reactivated_comeback_after_monday_pm() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.tvTitleReactivatedSeller).isViewDisplayed().withTextStr(
            context.getString(R.string.title_reactivated_seller_after_comeback)
        )
        onIdView(R.id.tvDescReactivatedSeller).isViewDisplayed().withTextStr(
            context.getString(R.string.desc_reactivated_seller_after_comeback)
        )
        onIdView(R.id.tvDescReactivatedSeller).isViewDisplayed().withTextStr(
            context.getString(R.string.desc_reactivated_seller_after_comeback)
        )
        onIdView(R.id.imgReactivatedSellerComeback).isViewDisplayed()
        onIdView(R.id.btnShopPerformanceLearn).isViewDisplayed()
        onIdView(R.id.icVideoPerformanceLearnReactivated).isViewDisplayed()
        onIdView(R.id.tvWatchVideoReactivated).isViewDisplayed().withTextStr(
            context.getString(R.string.title_tv_watch)
        )
    }

    @Test
    fun intended_youtube_page_click_btn_timer_reactivated_after_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.icVideoPerformanceLearnReactivated).onClick()
        Intents.intended(IntentMatchers.hasComponent(ShopPerformanceYoutubeActivity::class.java.name))
    }

    @Test
    fun intended_webview_page_click_btn_timer_reactivated_after_monday_os() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.btnShopPerformanceLearn).isViewDisplayed().onClick()
        intendingIntent()
    }

    @Test
    fun show_header_performance_when_reactivated_after_monday_pm() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        showHeaderPerformanceExistingPm(reactivatedAfterMondayPmResponse, shopInfoPeriodResponse)
    }

    @Test
    fun show_bottom_sheet_tooltip_level_reactivated_after_monday_pm() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_performance_level_information).onClick()
        showBottomSheetTooltipLevelPm(reactivatedAfterMondayPmResponse)
    }

    @Test
    fun show_bottom_sheet_tooltip_score_reactivated_after_monday_pm() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_shop_score_performance).onClick()
        showBottomSheetTooltipScoreExisting()
    }

    @Test
    fun show_section_period_detail_reactivated_after_monday_pm() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<PeriodDetailPerformanceUiModel>()
        showSectionPeriodDetailExisting(reactivatedAfterMondayPmResponse)
    }

    @Test
    fun show_item_detail_performance_and_show_bottomsheet_reactivated_after_monday_pm() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        showDetailPerformanceExisting()
    }

    @Test
    fun show_protected_parameter_reactivated_after_monday_pm() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<ProtectedParameterSectionUiModel>()
        showProtectedParameterSection(reactivatedAfterMondayPmResponse)
    }

    @Test
    fun show_power_merchant_section_after_monday_pm() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<ItemStatusPMUiModel>()
        showPowerMerchantSection()
    }

}