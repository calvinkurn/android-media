package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.tokopedia.applink.RouteManager
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.blockPromo
import com.tokopedia.topchat.chatroom.view.activity.base.hideBanner
import com.tokopedia.topchat.chatroom.view.activity.base.setFollowing
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcast.BroadcastResult.assertBroadcastShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertCarouselBundlingBroadcastShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertCtaOutOfStock
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertMultiBundlingBroadcastShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertSingleBundlingBroadcastShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingRobot.clickOnImageMultipleItemBundling
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingRobot.clickOnImageSingleItemBundling
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingRobot.doScrollProductBundlingToPositionInBroadcast
import org.junit.Test

@UiTest
class TopchatRoomProductBundlingBroadcastTest: TopchatRoomTest()  {

    @Test
    fun show_broadcast_multiple_product_bundling() {
        // Given
        getChatUseCase.response = getChatUseCase.broadCastChatWithProductBundlingResponse
            .blockPromo(false)
            .hideBanner(false)
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        assertBroadcastShown()
        assertMultiBundlingBroadcastShown()
    }

    @Test
    fun show_broadcast_single_product_bundling_with_multiple_item() {
        // Given
        getChatUseCase.response =
            getChatUseCase.broadCastChatWithSingleProductBundlingMultipleItemResponse
                .blockPromo(false)
                .hideBanner(false)
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        assertBroadcastShown()
        assertCarouselBundlingBroadcastShown(2)
    }

    @Test
    fun show_broadcast_single_product_bundling_with_single_item() {
        // Given
        getChatUseCase.response =
            getChatUseCase.broadCastChatWithSingleProductBundlingSingleItemResponse
                .blockPromo(false)
                .hideBanner(false)
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        assertBroadcastShown()
        assertSingleBundlingBroadcastShown()
    }

    @Test
    fun show_broadcast_single_product_bundling_with_oos() {
        // Given
        getChatUseCase.response =
            getChatUseCase.broadCastChatWithSingleProductBundlingOutOfStockResponse
                .blockPromo(false)
                .hideBanner(false)
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )

        // Then
        assertBroadcastShown()
        assertSingleBundlingBroadcastShown()
        assertCtaOutOfStock(false)
    }

    @Test
    fun should_open_product_intent_when_image_in_multiple_bundling_clicked() {
        // Given
        getChatUseCase.response =
            getChatUseCase.broadCastChatWithProductBundlingResponse
                .blockPromo(false)
                .hideBanner(false)
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        doScrollProductBundlingToPositionInBroadcast(1)
        clickOnImageMultipleItemBundling()

        // Then
        assertBroadcastShown()
        val intent = RouteManager.getIntent(context,
            "tokopedia://product/2149129024?extParam=whid%3D341730%26src%3Dchat") //Applink from JSON
        Intents.intended(IntentMatchers.hasData(intent.data))
    }

    @Test
    fun should_open_product_intent_when_image_in_single_bundling_clicked() {
        // Given
        getChatUseCase.response =
            getChatUseCase.broadCastChatWithSingleProductBundlingOutOfStockResponse
                .blockPromo(false)
                .hideBanner(false)
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        getShopFollowingUseCaseStub.response = getShopFollowingStatus.setFollowing(true)
        launchChatRoomActivity()
        Intents.intending(IntentMatchers.anyIntent()).respondWith(
            Instrumentation.ActivityResult(Activity.RESULT_OK, null)
        )
        clickOnImageSingleItemBundling()

        // Then
        assertBroadcastShown()
        val intent = RouteManager.getIntent(context,
            "tokopedia://product/2149129024?extParam=whid%3D341730%26src%3Dchat") //Applink from JSON
        Intents.intended(IntentMatchers.hasData(intent.data))
    }
}