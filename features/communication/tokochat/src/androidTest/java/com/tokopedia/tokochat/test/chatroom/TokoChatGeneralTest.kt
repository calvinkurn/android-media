package com.tokopedia.tokochat.test.chatroom

import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.tokochat.common.util.OrderStatusType
import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil
import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil.MAX_DISPLAYED_STRING
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.chatOrderHistoryTokoFoodResponse
import com.tokopedia.tokochat.stub.domain.response.GqlResponseStub.getNeedConsentResponse
import com.tokopedia.tokochat.test.base.BaseTokoChatRoomTest
import com.tokopedia.tokochat.test.chatroom.robot.consent.ConsentResult
import com.tokopedia.tokochat.test.chatroom.robot.consent.ConsentRobot
import com.tokopedia.tokochat.test.chatroom.robot.header.HeaderResult
import com.tokopedia.tokochat.test.chatroom.robot.message_bubble.MessageBubbleResult
import com.tokopedia.tokochat.test.chatroom.robot.reply_area.ReplyAreaResult
import com.tokopedia.tokochat.test.chatroom.robot.reply_area.ReplyAreaRobot
import org.junit.Test
import com.tokopedia.tokochat.test.R as tokochattestR
import com.tokopedia.tokochat_common.R as tokochat_commonR

@UiTest
class TokoChatGeneralTest : BaseTokoChatRoomTest() {

    @Test
    fun should_show_chat_room_header_with_censored_plat_number() {
        // When
        launchChatRoomActivity()

        // Then
        HeaderResult.assertHeaderDisplayed(
            interlocutorName = "Tokofood",
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
        chatOrderHistoryTokoFoodResponse.editAndGetResponseObject {
            it.tokochatOrderProgress.state = OrderStatusType.TOKOFOOD_COMPLETED
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
        HeaderResult.assertOrderHistoryTokoFood(
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
            activity.getString(tokochat_commonR.string.tokochat_desc_empty_text_box)
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
            activity.getString(tokochat_commonR.string.tokochat_desc_empty_text_box)
        )
    }

    @Test
    fun should_show_warning_offset_when_text_in_reply_area_more_than_offset() {
        // When
        launchChatRoomActivity()
        val longText = context.resources.getString(
            tokochattestR.string.tokochat_long_text
        )
        ReplyAreaRobot.replaceTextInReplyArea(longText)

        // Then
        ReplyAreaResult.assertReplyAreaErrorMessage(
            activity.getString(
                tokochat_commonR.string.tokochat_desc_max_char_exceeded,
                "1"
            )
        )
    }

    @Test
    fun should_show_warning_10_000_plus_when_text_in_reply_area_more_than_10_000() {
        // When
        launchChatRoomActivity()
        val longText = context.resources.getString(
            tokochattestR.string.tokochat_long_text
        )
        ReplyAreaRobot.replaceTextInReplyArea(longText.repeat(11))

        // Then
        ReplyAreaResult.assertReplyAreaErrorMessage(
            activity.getString(
                tokochat_commonR.string.tokochat_desc_max_char_exceeded,
                MAX_DISPLAYED_STRING
            )
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
        Thread.sleep(2000)

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

    @Test
    fun should_show_order_progress_even_when_param_tkpd_order_id_is_empty() {
        // Given
        val dummyApplink = "tokopedia://tokochat?${ApplinkConst.TokoChat.PARAM_SOURCE}=${TokoChatCommonValueUtil.SOURCE_TOKOFOOD}&${ApplinkConst.TokoChat.ORDER_ID_GOJEK}=$GOJEK_ORDER_ID_DUMMY"

        // When
        launchChatRoomActivity {
            it.data = Uri.parse(dummyApplink)
        }

        // Then
        HeaderResult.assertOrderHistoryTokoFood(
            merchantName = "TokoFood Outlet Test 1",
            timeDelivery = "17:35 - 17:38"
        )
        MessageBubbleResult.assertMessageBubbleVisibility(
            position = 0,
            isVisible = true
        )
        HeaderResult.assertHeaderDisplayed(
            interlocutorName = "Tokofood",
            licensePlate = "***1OAH"
        )
        HeaderResult.assertCallButtonHeader(isDisabled = false)
    }

    @Test
    fun should_show_local_load_order_progress_even_when_fail_to_translate_gojek_order_id() {
        // Given
        val dummyApplink = "tokopedia://tokochat?${ApplinkConst.TokoChat.PARAM_SOURCE}=${TokoChatCommonValueUtil.SOURCE_TOKOFOOD}&${ApplinkConst.TokoChat.ORDER_ID_GOJEK}=$GOJEK_ORDER_ID_DUMMY"
        GqlResponseStub.getTkpdOrderIdResponse.isError = true

        // When
        launchChatRoomActivity {
            it.data = Uri.parse(dummyApplink)
        }

        // Then
        HeaderResult.assertOrderHistoryLocalLoad()
        MessageBubbleResult.assertMessageBubbleVisibility(
            position = 0,
            isVisible = true
        )
        HeaderResult.assertHeaderDisplayed(
            interlocutorName = "Tokofood",
            licensePlate = "***1OAH"
        )
        HeaderResult.assertCallButtonHeader(isDisabled = true)
    }

    @Test
    fun should_show_order_progress_logistic() {
        // Given
        val dummyApplink = "tokopedia://tokochat?${ApplinkConst.TokoChat.PARAM_SOURCE}=${TokoChatCommonValueUtil.SOURCE_GOSEND_INSTANT}&${ApplinkConst.TokoChat.ORDER_ID_GOJEK}=$GOJEK_ORDER_ID_DUMMY"

        // When
        launchChatRoomActivity {
            it.data = Uri.parse(dummyApplink)
        }

        // Then
        HeaderResult.assertOrderHistoryLogistic(
            productName = "Barang Gosend Logistic",
            additionalInfo = "+1 produk lainnya",
            estimateLabel = "Estimasi tiba:",
            estimateValue = "16 Nov - 17 Nov, maks 13:00 WIB"
        )
    }

    @Test
    fun should_show_order_progress_logistic_without_additional_info() {
        // Given
        val dummyApplink = "tokopedia://tokochat?${ApplinkConst.TokoChat.PARAM_SOURCE}=${TokoChatCommonValueUtil.SOURCE_GOSEND_INSTANT}&${ApplinkConst.TokoChat.ORDER_ID_GOJEK}=$GOJEK_ORDER_ID_DUMMY"
        GqlResponseStub.chatOrderHistoryLogisticResponse.editAndGetResponseObject {
            it.tokochatOrderProgress.additionalInfo = ""
        }

        // When
        launchChatRoomActivity {
            it.data = Uri.parse(dummyApplink)
        }

        // Then
        HeaderResult.assertOrderHistoryLogistic(
            productName = "Barang Gosend Logistic",
            additionalInfo = "",
            estimateLabel = "Estimasi tiba:",
            estimateValue = "16 Nov - 17 Nov, maks 13:00 WIB"
        )
    }
}
