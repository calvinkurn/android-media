package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.stub.chatroom.view.presenter.TopChatRoomPresenterStub
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class TopchatRoomBuyerWebSocketTest : BaseBuyerTopchatRoomTest() {

    @Test
    fun sent_text_to_ws_and_got_response_from_ws() {
        // Given
        val myMsg = "Hi seller"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        changeResponseStartTime(
                wsMineResponseText, TopChatRoomPresenterStub.exStartTime
        )
        launchChatRoomActivity()

        // When
        onView(withId(R.id.new_comment))
                .perform(click())
                .perform(typeText(myMsg))
        onView(withId(R.id.send_but))
                .perform(click())
        websocket.simulateResponse(wsMineResponseText)

        // Then
        onView(withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                0, R.id.tvMessage
        )).check(matches(withText(myMsg)))
    }

    @Test
    fun received_normal_text_with_label_from_seller() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        changeResponseStartTime(
                wsMineResponseText, TopChatRoomPresenterStub.exStartTime
        )
        launchChatRoomActivity()

        // When
        websocket2.simulateResponse(wsSellerResponseText)

        // Then
        val label = wsSellerResponseText.jsonObject
                ?.get("label")?.asString
        onView(withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                0, R.id.txt_info
        )).check(matches(withText(label)))
    }

    @Test
    fun received_normal_text_without_label_from_seller() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        changeResponseStartTime(
                wsMineResponseText, TopChatRoomPresenterStub.exStartTime
        )
        launchChatRoomActivity()

        // When
        websocket.simulateResponse(wsSellerResponseText.setLabel(""))

        // Then
        onView(withRecyclerView(R.id.recycler_view_chatroom).atPositionOnView(
                0, R.id.txt_info
        )).check(matches(not(isDisplayed())))
    }

}
