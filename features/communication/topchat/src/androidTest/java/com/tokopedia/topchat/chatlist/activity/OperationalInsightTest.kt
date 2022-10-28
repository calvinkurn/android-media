package com.tokopedia.topchat.chatlist.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatlist.activity.base.ChatListTest
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightResult.assertOperationalInsightTickerNotVisible
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightResult.assertOperationalInsightTickerText
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightResult.assertOperationalInsightTickerVisible
import org.junit.Test
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightResult.assertBottomSheetVisible
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightResult.assertOperationalInsightBottomSheetText
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightRobot.clickOnCTABottomSheet
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightRobot.clickOnTextBottomSheet
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightRobot.clickOperationalInsightTicker

@UiTest
class OperationalInsightTest : ChatListTest() {
    @Test
    fun should_show_maintain_ticker_operational_insight_in_sellerapp() {
        //Given
        chatListUseCase.response = exSize5ChatListPojo
        getOperationalInsightUseCase.response = getOperationalInsightUseCase.getShopMetricResponse(
            isShowing = true,
            isMaintain = true
        )
        userSession.setIsShopOwner(true)

        //When
        startChatListActivity(isSellerApp = true)

        //Then
        assertOperationalInsightTickerVisible()
        assertOperationalInsightTickerText(context.getString(R.string.chat_performance_ticker_maintain))
    }

    @Test
    fun should_show_ticker_operational_insight_in_sellerapp() {
        //Given
        chatListUseCase.response = exSize5ChatListPojo
        getOperationalInsightUseCase.response = getOperationalInsightUseCase.getShopMetricResponse(
            isShowing = true,
            isMaintain = false
        )
        userSession.setIsShopOwner(true)

        //When
        startChatListActivity(isSellerApp = true)

        //Then
        assertOperationalInsightTickerVisible()
        assertOperationalInsightTickerText(context.getString(R.string.chat_performance_ticker))
    }

    @Test
    fun should_not_show_ticker_operational_insight_in_mainapp() {
        //Given
        chatListUseCase.response = exSize5ChatListPojo
        getOperationalInsightUseCase.response = getOperationalInsightUseCase.getShopMetricResponse(
            isShowing = true,
            isMaintain = true
        )
        userSession.setIsShopOwner(true)

        //When
        startChatListActivity()

        //Then
        assertOperationalInsightTickerNotVisible()
    }

    @Test
    fun should_not_show_ticker_operational_insight_when_isShowing_false() {
        //Given
        chatListUseCase.response = exSize5ChatListPojo
        getOperationalInsightUseCase.response = getOperationalInsightUseCase.getShopMetricResponse(
            isShowing = false,
            isMaintain = true
        )
        userSession.setIsShopOwner(true)

        //When
        startChatListActivity()

        //Then
        assertOperationalInsightTickerNotVisible()
    }

    @Test
    fun should_show_maintain_bottom_sheet_when_click_ticker() {
        //Given
        chatListUseCase.response = exSize5ChatListPojo
        getOperationalInsightUseCase.response = getOperationalInsightUseCase.getShopMetricResponse(
            isShowing = true,
            isMaintain = true
        )
        userSession.setIsShopOwner(true)
        startChatListActivity(isSellerApp = true)

        //When
        clickOperationalInsightTicker()

        //Then
        assertBottomSheetVisible()
        assertOperationalInsightBottomSheetText(BS_SUMMARY_MAINTAIN)
    }

    @Test
    fun should_show_bottom_sheet_when_click_ticker() {
        //Given
        chatListUseCase.response = exSize5ChatListPojo
        getOperationalInsightUseCase.response = getOperationalInsightUseCase.getShopMetricResponse(
            isShowing = true,
            isMaintain = false
        )
        userSession.setIsShopOwner(true)
        startChatListActivity(isSellerApp = true)

        //When
        clickOperationalInsightTicker()

        //Then
        assertBottomSheetVisible()
        assertOperationalInsightBottomSheetText(BS_SUMMARY_FIX)
    }

    @Test
    fun should_go_to_gold_merchant_statistic_page_when_cta_clicked() {
        //Given
        chatListUseCase.response = exSize5ChatListPojo
        getOperationalInsightUseCase.response = getOperationalInsightUseCase.getShopMetricResponse(
            isShowing = true,
            isMaintain = false
        )
        userSession.setIsShopOwner(true)
        startChatListActivity(isSellerApp = true)

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        clickOperationalInsightTicker()
        clickOnCTABottomSheet()

        //Then
        Intents.intended(IntentMatchers.hasData("tokopedia://gold-merchant-statistic-dashboard?page=operational-insight"))
    }

    @Test
    fun should_go_to_shop_score_page_when_href_text_clicked() {
        //Given
        chatListUseCase.response = exSize5ChatListPojo
        getOperationalInsightUseCase.response = getOperationalInsightUseCase.getShopMetricResponse(
            isShowing = true,
            isMaintain = false
        )
        userSession.setIsShopOwner(true)
        startChatListActivity(isSellerApp = true)

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        clickOperationalInsightTicker()
        clickOnTextBottomSheet(SHOP_PERFORMANCE)

        //Then
        Intents.intended(IntentMatchers.hasData("tokopedia://shop-score-detail?param=test123"))
    }

    companion object {
        private const val BS_SUMMARY_MAINTAIN = "Pertahankan Performa Chat sesuai target agar Performa Toko kamu tetap baik, ya."
        private const val BS_SUMMARY_FIX = "Tingkatkan Performa Chat sesuai target agar Performa Toko kamu lebih baik, ya."
        private const val SHOP_PERFORMANCE = "Performa Toko (9"
    }
}