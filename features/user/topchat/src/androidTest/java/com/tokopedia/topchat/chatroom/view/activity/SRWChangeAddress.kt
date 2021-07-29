package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import org.junit.Test

class SRWChangeAddress : TopchatRoomTest() {

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

    // TODO: should show cta disabled state when attachment status disabled
    // TODO: should show attachment body text as bubble message
    // TODO: should hide cta when attachment does not have visible cta
    // TODO: should resend SRW when user success change address
    // TODO: should hide msg header when attachment is null
}