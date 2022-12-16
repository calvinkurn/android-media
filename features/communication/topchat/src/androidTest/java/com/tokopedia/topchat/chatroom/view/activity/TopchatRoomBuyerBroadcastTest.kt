package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.blockPromo
import com.tokopedia.topchat.chatroom.view.activity.base.hideBanner
import com.tokopedia.topchat.chatroom.view.activity.base.setFollowing
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcast.BroadcastResult.assertBroadcastCtaText
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcast.BroadcastResult.assertBroadcastShown
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcast.BroadcastResult.assertBroadcastSpamHandler
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcast.BroadcastRobot.clickCtaBroadcast
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.openPageWithExtra
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class TopchatRoomBuyerBroadcastTest : TopchatRoomTest() {

    @Test
    fun show_broadcast_spam_handler_when_block_and_following_is_false() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(false)
        launchChatRoomActivity()
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        assertBroadcastSpamHandlerIsVisible()
    }

    @Test
    fun hide_broadcast_spam_handler_when_block_is_false_and_following_is_true() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        assertBroadcastSpamHandlerIsHidden()
    }

    @Test
    fun hide_broadcast_spam_handler_when_block_is_true_and_following_is_true() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(true)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        assertBroadcastSpamHandlerIsHidden()
    }

    @Test
    fun hide_broadcast_spam_handler_when_block_is_true_and_following_is_false() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer.blockPromo(true)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(false)
        launchChatRoomActivity()
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        assertBroadcastSpamHandlerIsHidden()
    }

    @Test
    fun show_broadcast_banner() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer
            .blockPromo(false)
            .hideBanner(false)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        onView(withId(R.id.iv_banner)).check(matches(isDisplayed()))
    }

    @Test
    fun hide_broadcast_banner() {
        // Given
        getChatUseCase.response = firstPageChatBroadcastAsBuyer
            .blockPromo(false)
            .hideBanner(true)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        onView(withId(R.id.iv_banner)).check(matches(not(isDisplayed())))
    }

    @Test
    fun show_broadcast_with_flexible_cta() {
        // Given
        getChatUseCase.response = getChatUseCase.broadCastChatWithFlexibleCta
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        intending(anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // When
        clickCtaBroadcast()

        // Then
        assertBroadcastCtaText("Lihat Keranjang")
        openPageWithExtra("url", "https://chat.tokopedia.com/tc/v1/redirect/broadcast_url/")
    }

    private fun assertBroadcastSpamHandlerIsVisible() {
        assertBroadcastSpamHandler()
        onView(withId(R.id.title_bc_handle)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_stop_promo)).check(matches(isDisplayed()))
        onView(withId(R.id.btn_follow_shop)).check(matches(isDisplayed()))
    }

    private fun assertBroadcastSpamHandlerIsHidden() {
        assertBroadcastShown()
        onView(withId(R.id.title_bc_handle)).check(doesNotExist())
        onView(withId(R.id.btn_stop_promo)).check(doesNotExist())
        onView(withId(R.id.btn_follow_shop)).check(doesNotExist())
    }
}
