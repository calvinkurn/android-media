package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import org.hamcrest.CoreMatchers.not
import org.junit.Test

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
        assertMsgBubbleAt(0, withText("Attachment Body Msg"))
    }

    // TODO: should hide cta when attachment does not have visible cta
    // TODO: should resend SRW when user success change address
    // TODO: should hide msg header when attachment is null
}