package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.stub.common.UserSessionStub
import org.junit.After
import org.junit.Test

class TopchatRoomBannedProductTest: BaseBuyerTopchatRoomTest() {

    private val redirectUrlTest = "https://www.tokopedia.com"

    @Test
    fun should_open_redirect_url_with_seamless_login_when_click_banned_product() {
        (userSession as UserSessionStub).loggedIn = true
        getKeygenUseCase.response = keygenResponse
        getChatUseCase.response = firstPageChatWithBannedProduct
        chatAttachmentUseCase.response = ChatAttachmentResponse()
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        scrollChatToPosition(0)
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(4, R.id.btn_buy)
        ).perform(ViewActions.click())

        //Then
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_VIEW))
    }

    @Test
    fun should_open_redirect_url_with_seamless_login_when_click_banned_product_but_fail() {
        (userSession as UserSessionStub).loggedIn = true
        getKeygenUseCase.errorMessage = "Oops!"
        getChatUseCase.response = firstPageChatWithBannedProduct
        chatAttachmentUseCase.response = ChatAttachmentResponse()
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        scrollChatToPosition(0)
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(4, R.id.btn_buy)
        ).perform(ViewActions.click())

        //Then
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrlTest))
        Intents.intended(IntentMatchers.hasData(intent.data))
    }

    @Test
    fun should_open_redirect_url_with_seamless_login_when_click_banned_product_but_notLoggedIn() {
        (userSession as UserSessionStub).loggedIn = false
        getKeygenUseCase.response = keygenResponse
        getChatUseCase.response = firstPageChatWithBannedProduct
        chatAttachmentUseCase.response = ChatAttachmentResponse()
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        scrollChatToPosition(0)
        onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(4, R.id.btn_buy)
        ).perform(ViewActions.click())

        //Then
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrlTest))
        Intents.intended(IntentMatchers.hasData(intent.data))
    }

    @After
    fun resetUserSession() {
        (userSession as UserSessionStub).loggedIn = true
    }
}