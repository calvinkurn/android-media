package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.test.application.matcher.hasTotalItemOf
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.replyBubbleResult
import com.tokopedia.topchat.matchers.isSender
import com.tokopedia.topchat.stub.chatroom.view.fragment.TopChatRoomFragmentStub
import com.tokopedia.topchat.test.R
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class SRWChangeAddressTest : TopchatRoomTest() {

    @Test
    fun should_show_attachment_title_when_msg_cta_attachment_is_not_null() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultChangeAddressResponse
        launchChatRoomActivity()

        // Then
        assertHeaderTitleMsgAtBubblePosition(0, isDisplayed())
        assertHeaderTitleMsgAtBubblePosition(0, withText("Pengiriman ke Pembeli"))
    }

    @Test
    fun should_show_cta_enabled_state_when_attachment_has_visible_cta() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultChangeAddressResponse
        launchChatRoomActivity()

        // Then
        assertCtaHeaderMsgAtBubblePosition(0, isDisplayed())
        assertCtaHeaderMsgAtBubblePosition(0, withText("Ubah alamat"))
        assertCtaHeaderMsgAtBubblePosition(0, isEnabled())
    }

    @Test
    fun should_show_cta_disabled_state_when_attachment_status_disabled() {
        // Given
        getChatUseCase.response = getChatUseCase.srwChangeAddressCtaDisabled
        launchChatRoomActivity()

        // Then
        assertCtaHeaderMsgAtBubblePosition(0, isDisplayed())
        assertCtaHeaderMsgAtBubblePosition(0, withText("Disabled"))
        assertCtaHeaderMsgAtBubblePosition(0, not(isEnabled()))
    }

    @Test
    fun should_show_attachment_body_text_as_bubble_message() {
        // Given
        getChatUseCase.response = getChatUseCase.srwChangeAddressBodyMsg
        launchChatRoomActivity()

        // Then
        replyBubbleResult {
            assertMsgBubbleAt(0, withText("Attachment Body Msg"))
        }
    }

    @Test
    fun should_hide_cta_when_attachment_does_not_have_visible_cta() {
        // Given
        getChatUseCase.response = getChatUseCase.srwChangeAddressNoCta
        launchChatRoomActivity()

        // Then
        assertCtaHeaderMsgAtBubblePosition(0, not(isDisplayed()))
    }

    @Test
    fun should_resend_SRW_when_user_success_change_address() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultChangeAddressResponse
        TopChatRoomFragmentStub.SUCCESS_CHANGE_ADDRESS = true
        launchChatRoomActivity()
        val totalItemList = activity.getTotalItemInChat()

        // When
        clickCtaHeaderMsgAtBubblePosition(0)

        // Then
        assertChatRecyclerview(hasTotalItemOf(totalItemList + 1))
        assertChatRecyclerview(isSender(0))
    }

    @Test
    fun should_hide_msg_header_when_attachment_is_null() {
        // Given
        getChatUseCase.response = getChatUseCase.nullAttachment
        launchChatRoomActivity()

        // Then
        assertMsgHeaderContainer(0, not(isDisplayed()))
    }

    @Test
    fun should_show_msg_header_divider_if_i_am_buyer() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultChangeAddressResponse
        launchChatRoomActivity()

        // Then
        assertDividerHeaderContainer(0, isDisplayed())
    }

    @Test
    fun should_hide_msg_header_divider_if_i_am_seller() {
        // Given
        getChatUseCase.response = getChatUseCase.srwChangeAddressSeller
        launchChatRoomActivity()

        // Then
        assertDividerHeaderContainer(0, not(isDisplayed()))
    }

    @Test
    fun should_show_success_toaster_when_success_change_address() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultChangeAddressResponse
        TopChatRoomFragmentStub.SUCCESS_CHANGE_ADDRESS = true
        launchChatRoomActivity()
        val expectedMsg = context.getString(R.string.toaster_success_chosen_address)

        // When
        clickCtaHeaderMsgAtBubblePosition(0)

        // Then
        generalResult {
            assertToasterText(expectedMsg)
        }
    }
}
