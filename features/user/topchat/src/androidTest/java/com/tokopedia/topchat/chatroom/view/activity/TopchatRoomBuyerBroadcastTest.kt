package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.topchat.R
import com.tokopedia.topchat.assertion.atPositionIsInstanceOf
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.blockPromo
import com.tokopedia.topchat.chatroom.view.activity.base.hideBanner
import com.tokopedia.topchat.chatroom.view.activity.base.setFollowing
import com.tokopedia.topchat.chatroom.view.uimodel.BroadCastUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.BroadcastSpamHandlerUiModel
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class TopchatRoomBuyerBroadcastTest : TopchatRoomTest() {

    @Test
    fun show_broadcast_spam_handler_when_block_and_following_is_false() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(false)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        inflateTestFragment()

        // Then
        assertBroadcastSpamHandlerIsVisible()
    }

    @Test
    fun hide_broadcast_spam_handler_when_block_is_false_and_following_is_true() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        inflateTestFragment()

        // Then
        assertBroadcastSpamHandlerIsHidden()
    }

    @Test
    fun hide_broadcast_spam_handler_when_block_is_true_and_following_is_true() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(true)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        inflateTestFragment()

        // Then
        assertBroadcastSpamHandlerIsHidden()
    }

    @Test
    fun hide_broadcast_spam_handler_when_block_is_true_and_following_is_false() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(true)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(false)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        inflateTestFragment()

        // Then
        assertBroadcastSpamHandlerIsHidden()
    }

    @Test
    fun show_broadcast_banner() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatBroadcastAsBuyer
                .blockPromo(false)
                .hideBanner(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        inflateTestFragment()

        // Then
        onView(withId(R.id.iv_banner)).check(matches(isDisplayed()))
    }

    @Test
    fun hide_broadcast_banner() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatBroadcastAsBuyer
                .blockPromo(false)
                .hideBanner(true)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        inflateTestFragment()

        // Then
        onView(withId(R.id.iv_banner)).check(matches(not(isDisplayed())))
    }

    private fun assertBroadcastSpamHandlerIsVisible() {
        onView(withId(R.id.recycler_view)).check(
                atPositionIsInstanceOf(0, BroadcastSpamHandlerUiModel::class.java)
        )
        onView(withId(R.id.title_bc_handle)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_stop_promo)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_follow_shop)).check(matches(isDisplayed()))
    }

    private fun assertBroadcastSpamHandlerIsHidden() {
        onView(withId(R.id.recycler_view)).check(
                atPositionIsInstanceOf(0, BroadCastUiModel::class.java)
        )
        onView(withId(R.id.title_bc_handle)).check(doesNotExist())
        onView(withId(R.id.btn_stop_promo)).check(doesNotExist())
        onView(withId(R.id.btn_follow_shop)).check(doesNotExist())
    }
}
