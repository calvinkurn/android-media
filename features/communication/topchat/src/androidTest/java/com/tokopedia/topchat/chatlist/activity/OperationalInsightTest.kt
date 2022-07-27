package com.tokopedia.topchat.chatlist.activity

import com.tokopedia.topchat.chatlist.activity.base.ChatListTest
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightResult.assertOperationalInsightTickerNotVisible
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightResult.assertOperationalInsightTickerText
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightResult.assertOperationalInsightTickerVisible
import org.junit.Test
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightResult.assertBottomSheetVisible
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightResult.assertOperationalInsightBottomSheetText
import com.tokopedia.topchat.chatlist.activity.robot.operational_insight.OperationalInsightRobot.clickOperationalInsightTicker

class OperationalInsightTest : ChatListTest() {
    @Test
    fun should_show_maintain_ticker_operational_insight_in_sellerapp() {
        //Given
        chatListUseCase.response = exSize5ChatListPojo
        getOperationalInsightUseCase.response = getOperationalInsightUseCase.getShopMetricResponse(
            isShowing = true,
            isMaintain = true
        )

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

        //When
        startChatListActivity(isSellerApp = true)
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

        //When
        startChatListActivity(isSellerApp = true)
        clickOperationalInsightTicker()

        //Then
        assertBottomSheetVisible()
        assertOperationalInsightBottomSheetText(BS_SUMMARY_FIX)
    }

    companion object {
        private const val BS_SUMMARY_MAINTAIN = "Pertahankan Performa Chat sesuai target agar Performa Toko kamu tetap baik, ya."
        private const val BS_SUMMARY_FIX = "Tingkatkan Performa Chat sesuai target agar Performa Toko kamu lebih baik, ya."
    }
}