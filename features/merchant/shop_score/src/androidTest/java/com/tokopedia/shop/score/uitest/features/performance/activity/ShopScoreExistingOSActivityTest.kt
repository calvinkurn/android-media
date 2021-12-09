package com.tokopedia.shop.score.uitest.features.performance.activity

import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.uitest.features.performance.base.ShopScoreTest
import com.tokopedia.shop.score.uitest.stub.common.util.onClick
import com.tokopedia.shop.score.uitest.stub.common.util.onIdView
import com.tokopedia.shop.score.uitest.stub.common.util.scrollTo
import org.junit.Test

class ShopScoreExistingOSActivityTest: ShopScoreTest() {

    override fun setup() {
        super.setup()
        getShopInfoPeriodUseCaseStub.responseStub = shopInfoPeriodResponse
        getShopPerformanceUseCaseStub.responseStub = existingSellerOsResponse
    }

    @Test
    fun show_coachmark_when_existing_official_store() {
        shopScorePrefManagerStub.setFinishCoachMark(false)
        activityRule.launchActivity(getShopPerformancePageIntent())
        Thread.sleep(3000)
        showCoachMarkShopScoreOs()
    }

    @Test
    fun show_header_performance_when_existing_official_store() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        showHeaderPerformanceExisting(existingSellerOsResponse, shopInfoPeriodResponse)
    }

    @Test
    fun show_bottom_sheet_tooltip_level_existing() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_performance_level_information).onClick()
        showBottomSheetTooltipLevel(existingSellerOsResponse)
    }

    @Test
    fun show_bottom_sheet_tooltip_score_existing() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_shop_score_performance).onClick()
        showBottomSheetTooltipScoreExisting()
    }

    @Test
    fun show_section_period_detail_existing() {
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<PeriodDetailPerformanceUiModel>()
        showSectionPeriodDetailExisting(existingSellerOsResponse)
    }

}