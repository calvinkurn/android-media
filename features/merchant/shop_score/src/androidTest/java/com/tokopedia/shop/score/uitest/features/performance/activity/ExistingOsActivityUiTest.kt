package com.tokopedia.shop.score.uitest.features.performance.activity

import com.tokopedia.shop.score.R
import com.tokopedia.shop.score.performance.presentation.model.PeriodDetailPerformanceUiModel
import com.tokopedia.shop.score.performance.presentation.model.SectionFaqUiModel
import com.tokopedia.shop.score.uitest.features.performance.base.ShopScoreUiTest
import com.tokopedia.shop.score.stub.common.util.onClick
import com.tokopedia.shop.score.stub.common.util.onIdView
import com.tokopedia.shop.score.stub.common.util.scrollTo
import com.tokopedia.test.application.annotations.UiTest
import org.junit.Test

@UiTest
class ExistingOsActivityUiTest: ShopScoreUiTest() {

    override fun setup() {
        super.setup()
        getShopInfoPeriodUseCaseStub.responseStub = shopInfoPeriodResponse
        getShopPerformanceUseCaseStub.responseStub = existingSellerOsResponse
    }

    @Test
    fun show_coachmark_when_existing_os() {
        shopScorePrefManagerStub.setFinishCoachMark(false)
        activityRule.launchActivity(getShopPerformancePageIntent())
        showCoachMarkShopScore()
    }

    @Test
    fun show_header_performance_when_existing_os() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        showHeaderPerformanceExistingOs(existingSellerOsResponse, shopInfoPeriodResponse)
    }

    @Test
    fun show_bottom_sheet_tooltip_level_existing_os() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_performance_level_information).onClick()
        showBottomSheetTooltipLevelOs(existingSellerOsResponse)
    }

    @Test
    fun show_bottom_sheet_tooltip_score_existing_os() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        onIdView(R.id.ic_shop_score_performance).onClick()
        showBottomSheetTooltipScoreExisting()
    }

    @Test
    fun show_section_period_detail_existing_os() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<PeriodDetailPerformanceUiModel>()
        showSectionPeriodDetailExisting(existingSellerOsResponse)
    }

    @Test
    fun show_item_detail_performance_and_show_bottomsheet_os() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        showDetailPerformanceExisting()
    }

    @Test
    fun show_faq_shop_score_existing_os() {
        shopScorePrefManagerStub.setFinishCoachMark(true)
        activityRule.launchActivity(getShopPerformancePageIntent())
        activityRule.activity.scrollTo<SectionFaqUiModel>()
        showFaqItemList()
    }


}