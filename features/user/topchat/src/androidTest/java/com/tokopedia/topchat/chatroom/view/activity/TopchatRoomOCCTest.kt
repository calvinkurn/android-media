package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.stub.chatroom.view.fragment.TopChatRoomFragmentStub
import org.junit.After
import org.junit.Test

class TopchatRoomOCCTest: BaseBuyerTopchatRoomTest() {

    @Test
    fun should_directly_open_occ_when_click_beli_langsung_in_attached_product() {
        // Given
        TopChatRoomFragmentStub.isOCCActive = true
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        scrollChatToPosition(0)
        Espresso.onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(1, R.id.tv_buy)
        ).perform(ViewActions.click())

        //Then
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.ONE_CLICK_CHECKOUT,
            "1160424090"
        )
        Intents.intended(IntentMatchers.hasData(intent.data))
    }

    @Test
    fun should_show_toaster_when_click_beli_langsung_in_attached_product_and_fail() {
        // Given
        val expectedErrorMessage = "Oops OCC!"
        TopChatRoomFragmentStub.isOCCActive = true
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        addToCartOccMultiUseCase.errorMessage = listOf(expectedErrorMessage)
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        scrollChatToPosition(0)
        Espresso.onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(1, R.id.tv_buy)
        ).perform(ViewActions.click())

        //Then
        onView(withSubstring(expectedErrorMessage))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun should_show_toaster_when_click_beli_langsung_in_attached_product_and_error() {
        // Given
        TopChatRoomFragmentStub.isOCCActive = true
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        addToCartOccMultiUseCase.isError = true
        launchChatRoomActivity()

        //When
        Intents.intending(IntentMatchers.anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        scrollChatToPosition(0)
        Espresso.onView(
            withRecyclerView(R.id.recycler_view_chatroom)
                .atPositionOnView(1, R.id.tv_buy)
        ).perform(ViewActions.click())

        //Then
        onView(withSubstring("Oops!"))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @After
    fun after() {
        TopChatRoomFragmentStub.isOCCActive = false
    }

}