package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import org.junit.Test

class SRWChangeAddress : TopchatRoomTest() {

    // TODO: should show attachment title when msg cta attachment is not null

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