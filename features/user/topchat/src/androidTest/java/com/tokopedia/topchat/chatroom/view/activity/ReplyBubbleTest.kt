package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import org.junit.Test

class ReplyBubbleTest : TopchatRoomTest() {

    @Test
    fun should_show_bottomsheet_menu_on_long_click_normal_text_bubble() {
        // Given
        getChatUseCase.response = getChatUseCase.defaultReplyBubbleResponse
        launchChatRoomActivity()

        // When
        longClickBubbleAt(1)

        // Then
        assertLongClickMenu(isDisplayed())
    }

    // TODO: should disable long click on fraud status msg true from ws
    // TODO: should show reply bubble compose when user long click and reply
    // TODO: should hide or cancel reply compose when user click close icon
    // TODO: should not sent closed reply compose to websocket
    // TODO: should go to specific bubble when reply compose is clicked
    // TODO: should sent and render reply bubble when user sent normal text
    // TODO: should show normal text reply bubble when parent reply is not null from GQL
    // TODO: should show text reply bubble when parent reply is not null from websocket
    // TODO: should sent and render reply bubble when user sent sticker
    // TODO: should show sticker reply bubble when parent reply is not null from GQL
    // TODO: should show sticker reply bubble when parent reply is not null from websocket
    // TODO: should show image reply bubble when parent reply is not null from GQL
    // TODO: should show image reply bubble when parent reply is not null from websocket
    // TODO: should match the senderId name with contacts from chatReplies GQL
    // TODO: should go to specific bubble when msg bubble local id is exist
    // TODO: should reset chatroom page like chat search when click reply bubble from GQL (ioe, local id is not exist)
    // TODO: should able copy to clipboard msg bubble
    // TODO: should show expired toaster when user click expired reply bubble

}