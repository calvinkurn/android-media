package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.changeCtaBroadcast
import com.tokopedia.topchat.chatroom.view.activity.base.setFollowing
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcast.BroadcastResult
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcast.BroadcastRobot
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
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // When
        BroadcastRobot.clickCtaBroadcast()

        // Then
        BroadcastResult.assertBroadcastCtaText("Lihat Keranjang")
        BroadcastResult.assertBroadcastCtaLabel(true)
    }

    @Test
    fun show_broadcast_with_flexible_cta_but_null_on_seller_side() {
        // Given
        getChatUseCase.response = getChatUseCase.broadCastChatWithFlexibleCtaSeller
            .changeCtaBroadcast(null, null, null)
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity(isSellerApp = true)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // When
        BroadcastRobot.clickCtaBroadcast()

        // Then
        BroadcastResult.assertBroadcastCtaText("Lihat Selengkapnya")
        BroadcastResult.assertBroadcastCtaLabel(false)
    }

    @Test
    fun show_broadcast_with_flexible_cta_but_empty_on_seller_side() {
        // Given
        getChatUseCase.response = getChatUseCase.broadCastChatWithFlexibleCtaSeller
            .changeCtaBroadcast(null, "", "")
        chatAttachmentUseCase.response = chatAttachmentResponse
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity(isSellerApp = true)
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // When
        BroadcastRobot.clickCtaBroadcast()

        // Then
        BroadcastResult.assertBroadcastCtaText("Lihat Selengkapnya")
        BroadcastResult.assertBroadcastCtaLabel(false)
    }
}
