package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.action.RecyclerViewChildActions.Companion.atPositionOnView
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.stub.chatroom.view.presenter.TopChatRoomPresenterStub
import com.tokopedia.websocket.WebSocketResponse
import org.junit.Test

class TopchatRoomBuyerWebSocketTest : TopchatRoomTest() {

    private var wsResponseText: WebSocketResponse = AndroidFileUtil.parse(
            "ws_response_text.json",
            WebSocketResponse::class.java
    )

    @Test
    fun sent_text_to_ws_and_got_response_from_ws() {
        // Given
        setupChatRoomActivity()
        val myMsg = "Hi seller"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        changeResponseStartTime(
                wsResponseText, TopChatRoomPresenterStub.exStartTime
        )
        inflateTestFragment()

        // When
        onView(withId(R.id.new_comment))
                .perform(click())
                .perform(typeText(myMsg))
        onView(withId(R.id.send_but))
                .perform(click())
        websocket.simulateResponse(wsResponseText)
        waitForIt(RV_DELAY)

        // Then
        onView(withId(R.id.recycler_view)).check(
                matches(
                        atPositionOnView(0, withText(myMsg), R.id.tvMessage)
                )
        )
    }

}