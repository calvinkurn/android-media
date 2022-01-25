package com.tokopedia.shop.score.uitest.features.performance.activity

import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.ItemStatusPMUiModel
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.uitest.features.performance.base.ShopScoreTest
import com.tokopedia.shop.score.uitest.stub.common.util.onClick
import com.tokopedia.shop.score.uitest.stub.common.util.onIdView
import com.tokopedia.shop.score.uitest.stub.common.util.scrollTo
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class ExistingPmActivityTest : ShopScoreTest() {

    override fun setup() {
        super.setup()
        getShopInfoPeriodUseCaseStub.responseStub = shopInfoPeriodResponse
        getShopPerformanceUseCaseStub.responseStub = existingSellerPmResponse
    }

    @Test
    fun show_coachmark_when_existing_pm() {
        shopScorePrefManagerStub.setFinishCoachMark(false)
        activityRule.launchActivity(getShopPerformancePageIntent())
        showCoachMarkShopScore()
    }

    @Test
    fun show_header_performance_when_existing_pm() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        showHeaderPerformanceExistingPm(existingSellerPmResponse, shopInfoPeriodResponse)
    }

    @Test
    fun show_bottom_sheet_tooltip_level_existing_pm() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_performance_level_information).onClick()
        showBottomSheetTooltipLevelPm(existingSellerPmResponse)
    }

    @Test
    fun show_bottom_sheet_tooltip_score_existing_pm() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_shop_score_performance).onClick()
        showBottomSheetTooltipScoreExisting()
    }

    @Test
    fun show_section_period_detail_existing_pm() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<PeriodDetailPerformanceUiModel>()
        showSectionPeriodDetailExisting(existingSellerPmResponse)
    }

    @Test
    fun show_item_detail_performance_and_show_bottomsheet_pm() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        showDetailPerformanceExisting()
    }

    @Test
    fun show_power_merchant_section() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<ItemStatusPMUiModel>()
        showPowerMerchantSection()
    }
}