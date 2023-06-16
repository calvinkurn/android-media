package com.tokopedia.tokochat.test

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.chatOrderHistoryResponse
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.getNeedConsentResponse
import com.tokopedia.tokochat.test.base.BaseTokoChatTest
import com.tokopedia.tokochat.test.robot.consent.ConsentResult
import com.tokopedia.tokochat.test.robot.consent.ConsentRobot
import com.tokopedia.tokochat.test.robot.header.HeaderResult
import com.tokopedia.tokochat.test.robot.reply_area.ReplyAreaResult
import com.tokopedia.tokochat.test.robot.reply_area.ReplyAreaRobot
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.util.OrderStatusType
import com.tokopedia.tokochat_common.util.TokoChatValueUtil.MAX_DISPLAYED_STRING
import org.junit.Test

@UiTest
class TokoChatGeneralTest : BaseTokoChatTest() {

    @Test
    fun should_show_chat_room_header_with_censored_plat_number() {
        // When
        launchChatRoomActivity()

        // Then
        HeaderResult.assertHeaderDisplayed(
            interlocutorName = "Tokofood Driver 13",
            licensePlate = "***1OAH"
        )
    }

    @Test
    fun should_show_enabled_call_button() {
        // When
        launchChatRoomActivity()

        // Then
        HeaderResult.assertCallButtonHeader(isDisabled = false)
    }

    @Test
    fun should_show_disabled_call_button() {
        // Given
        chatOrderHistoryResponse.editAndGetResponseObject {
            it.tokochatOrderProgress.state = OrderStatusType.COMPLETED
        }

        // When
        launchChatRoomActivity()

        // Then
        HeaderResult.assertCallButtonHeader(isDisabled = true)
    }

    @Test
    fun should_show_order_progress() {
        // When
        launchChatRoomActivity()

        // Then
        HeaderResult.assertOrderHistory(
            merchantName = "TokoFood Outlet Test 1",
            timeDelivery = "17:35 - 17:38"
        )
    }

    @Test
    fun should_show_reply_area_and_can_type() {
        // When
        val dummyText = "Ditunggu ya"
        launchChatRoomActivity()
        ReplyAreaRobot.typeInReplyArea(dummyText)

        // Then
        ReplyAreaResult.assertReplyAreaIsDisplayed()
        ReplyAreaResult.assertTypeReplyAreaText(dummyText)
    }

    @Test
    fun should_enable_button_send_when_reply_area_is_not_empty() {
        // When
        val dummyText = "Ditunggu ya"
        launchChatRoomActivity()
        ReplyAreaRobot.clearReplyArea()
        ReplyAreaRobot.typeInReplyArea(dummyText)

        // Then
        ReplyAreaResult.assertNoSnackbarText(
            activity.getString(R.string.tokochat_desc_empty_text_box)
        )
    }

    @Test
    fun should_disable_button_send_when_reply_area_is_empty() {
        // When
        launchChatRoomActivity()
        ReplyAreaRobot.typeInReplyArea("Test 123")
        ReplyAreaRobot.clearReplyArea()
        ReplyAreaRobot.clickReplyButton()

        // Then
        ReplyAreaResult.assertSnackbarText(
            activity.getString(R.string.tokochat_desc_empty_text_box)
        )
    }

    @Test
    fun should_show_warning_offset_when_text_in_reply_area_more_than_offset() {
        // When
        launchChatRoomActivity()
        val longText = context.resources.getString(
            com.tokopedia.tokochat.test.R.string.tokochat_long_text
        )
        ReplyAreaRobot.replaceTextInReplyArea(longText)

        // Then
        ReplyAreaResult.assertReplyAreaErrorMessage(
            activity.getString(R.string.tokochat_desc_max_char_exceeded, "1")
        )
    }

    @Test
    fun should_show_warning_10_000_plus_when_text_in_reply_area_more_than_10_000() {
        // When
        launchChatRoomActivity()
        val longText = context.resources.getString(
            com.tokopedia.tokochat.test.R.string.tokochat_long_text
        )
        ReplyAreaRobot.replaceTextInReplyArea(longText.repeat(11))

        // Then
        ReplyAreaResult.assertReplyAreaErrorMessage(
            activity.getString(R.string.tokochat_desc_max_char_exceeded, MAX_DISPLAYED_STRING)
        )
    }

    @Test
    fun should_show_consent_bottomsheet_for_first_time_user() {
        // Given
        getNeedConsentResponse.editAndGetResponseObject {
            it.data.collectionPoints.first().needConsent = true
        }

        // When
        launchChatRoomActivity()

        // Then
        ConsentResult.assertConsentChatBottomSheet(isDisplayed = true)

        // When
        ConsentRobot.clickCheckBoxConsent()
        ConsentRobot.clickSubmitConsent()
        Thread.sleep(1000)

        // Then
        ConsentResult.assertConsentChatBottomSheet(isDisplayed = false)
    }

    @Test
    fun should_not_show_consent_bottomsheet_for_recurring_time_user() {
        // When
        launchChatRoomActivity()

        // Then
        ConsentResult.assertConsentChatBottomSheet(isDisplayed = false)
    }
}
