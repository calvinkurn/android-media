package com.tokopedia.topchat.chatroom.view.activity.test.seller

import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.changeCtaBroadcast
import com.tokopedia.topchat.chatroom.view.activity.base.setFollowing
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcastResult
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcastRobot
import org.junit.Test

class TopchatRoomSellerBroadcastTest: TopchatRoomTest() {

    @Test
    fun show_broadcast_with_flexible_cta_on_seller_side() {
        // Given
        getChatUseCase.response = getChatUseCase.broadCastChatWithFlexibleCtaSeller
            .changeCtaBroadcast("https://chat.tokopedia.com/tc/v1/redirect/broadcast_url/",
                "Lihat Keranjang", "Khusus Pembeli")
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity(isSellerApp = true)
        stubIntents()

        // When
        broadcastRobot {
            clickCtaBroadcast(0)
        }

        // Then
        broadcastResult {
            assertBroadcastCtaText("Lihat Keranjang")
            assertBroadcastCtaLabel(true)
        }
    }

    @Test
    fun show_broadcast_with_flexible_cta_but_null_on_seller_side() {
        // Given
        getChatUseCase.response = getChatUseCase.broadCastChatWithFlexibleCtaSeller
            .changeCtaBroadcast(null, null, null)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity(isSellerApp = true)
        stubIntents()

        // When
        broadcastRobot {
            clickCtaBroadcast(0)
        }

        // Then
        broadcastResult {
            assertBroadcastCtaText("Lihat Selengkapnya")
            assertBroadcastCtaLabel(false)
        }
    }

    @Test
    fun show_broadcast_with_flexible_cta_but_empty_on_seller_side() {
        // Given
        getChatUseCase.response = getChatUseCase.broadCastChatWithFlexibleCtaSeller
            .changeCtaBroadcast(null, "", "")
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity(isSellerApp = true)
        stubIntents()

        // When
        broadcastRobot {
            clickCtaBroadcast(0)
        }

        // Then
        broadcastResult {
            assertBroadcastCtaText("Lihat Selengkapnya")
            assertBroadcastCtaLabel(false)
        }
    }
}
