package com.tokopedia.topchat.chatroom.view.activity

import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.blockPromo
import com.tokopedia.topchat.chatroom.view.activity.base.changeCtaBroadcast
import com.tokopedia.topchat.chatroom.view.activity.base.hideBanner
import com.tokopedia.topchat.chatroom.view.activity.base.setFollowing
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcastResult
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcastRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
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
        stubIntents()

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
        stubIntents()

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
        stubIntents()

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
        stubIntents()

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
        stubIntents()

        // Then
        broadcastResult {
            assertBroadcastBanner(0, isDisplayed())
        }
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
        stubIntents()

        // Then
        broadcastResult {
            assertBroadcastBanner(0, not(isDisplayed()))
        }
    }

    @Test
    fun show_broadcast_with_flexible_cta() {
        // Given
        getChatUseCase.response = getChatUseCase.broadCastChatWithFlexibleCta
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        stubIntents()

        // When
        broadcastRobot {
            clickCtaBroadcast()
        }

        // Then
        broadcastResult {
            assertBroadcastCtaText("Lihat Keranjang")
            assertBroadcastCtaLabel(false)
        }
        generalResult {
            openPageWithExtra("url", "https://chat.tokopedia.com/tc/v1/redirect/broadcast_url/")
        }
    }

    @Test
    fun show_broadcast_with_flexible_cta_but_null() {
        // Given
        getChatUseCase.response = getChatUseCase.broadCastChatWithFlexibleCta
            .changeCtaBroadcast(null, null, null)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        stubIntents()

        // When
        broadcastRobot {
            clickCtaBroadcast()
        }

        // Then
        broadcastResult {
            assertBroadcastCtaText("Lihat Selengkapnya")
            assertBroadcastCtaLabel(false)
        }
        generalResult {
            openPageWithExtra("url", "https://chat.tokopedia.com/tc/v1/redirect/original_url/")
        }
    }

    @Test
    fun show_broadcast_with_flexible_cta_but_empty() {
        // Given
        getChatUseCase.response = getChatUseCase.broadCastChatWithFlexibleCta
            .changeCtaBroadcast(null, "", "")
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        stubIntents()

        // When
        broadcastRobot {
            clickCtaBroadcast()
        }

        // Then
        broadcastResult {
            assertBroadcastCtaText("Lihat Selengkapnya")
            assertBroadcastCtaLabel(false)
        }
    }

    private fun assertBroadcastSpamHandlerIsVisible() {
        broadcastResult {
            assertBroadcastSpamHandler()
            assertBroadcastTitleHandle(0, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            assertBroadcastBtnStopPromo(0, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            assertBroadcastBtnFollowShop(0, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        }
    }

    private fun assertBroadcastSpamHandlerIsHidden() {
        broadcastResult {
            assertBroadcastShown()
            assertBroadcastTitleHandleNotExist(0)
            assertBroadcastBtnStopPromoNotExist(0)
            assertBroadcastBtnFollowShopNotExist(0)
        }
    }
}
