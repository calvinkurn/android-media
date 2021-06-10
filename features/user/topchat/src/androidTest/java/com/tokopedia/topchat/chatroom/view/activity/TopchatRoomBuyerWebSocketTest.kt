package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.stub.chatroom.view.presenter.TopChatRoomPresenterStub
import com.tokopedia.websocket.WebSocketResponse
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class TopchatRoomBuyerWebSocketTest : BaseBuyerTopchatRoomTest() {

    private var wsMineResponseText: WebSocketResponse = WebSocketResponse()
    private var wsSellerResponseText: WebSocketResponse = WebSocketResponse()

    override fun setupResponse() {
        super.setupResponse()
        wsMineResponseText = AndroidFileUtil.parse(
                "ws_response_text.json",
                WebSocketResponse::class.java
        )
        wsSellerResponseText = AndroidFileUtil.parse(
                "buyer/ws_opposite_with_label.json",
                WebSocketResponse::class.java
        )
    }

    @Test
    fun sent_text_to_ws_and_got_response_from_ws() {
        // Given
        setupChatRoomActivity()
        val myMsg = "Hi seller"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        changeResponseStartTime(
                wsMineResponseText, TopChatRoomPresenterStub.exStartTime
        )
        inflateTestFragment()

        // When
        onView(withId(R.id.new_comment))
                .perform(click())
                .perform(typeText(myMsg))
        onView(withId(R.id.send_but))
                .perform(click())
        websocket.simulateResponse(wsMineResponseText)

        // Then
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(
                0, R.id.tvMessage
        )).check(matches(withText(myMsg)))
    }

    @Test
    fun received_normal_text_with_label_from_seller() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        changeResponseStartTime(
                wsMineResponseText, TopChatRoomPresenterStub.exStartTime
        )
        inflateTestFragment()

        // When
        websocket.simulateResponse(wsSellerResponseText)

        // Then
        val label = wsSellerResponseText.jsonObject
                ?.get("label")?.asString
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(
                0, R.id.txt_info
        )).check(matches(withText(label)))
    }

    @Test
    fun received_normal_text_without_label_from_seller() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        changeResponseStartTime(
                wsMineResponseText, TopChatRoomPresenterStub.exStartTime
        )
        inflateTestFragment()

        // When
        websocket.simulateResponse(wsSellerResponseText.setLabel(""))

        // Then
        onView(withRecyclerView(R.id.recycler_view).atPositionOnView(
                0, R.id.txt_info
        )).check(matches(not(isDisplayed())))
    }

    protected fun WebSocketResponse.setLabel(label: String): WebSocketResponse {
        jsonObject?.remove("label")
        jsonObject?.addProperty("label", label)
        return this
    }

}
