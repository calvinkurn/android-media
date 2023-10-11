package com.tokopedia.topchat.chatroom.view.activity.test

import com.tokopedia.applink.RouteManager
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.base.blockPromo
import com.tokopedia.topchat.chatroom.view.activity.base.hideBanner
import com.tokopedia.topchat.chatroom.view.activity.base.setFollowing
import com.tokopedia.topchat.chatroom.view.activity.robot.broadcastResult
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.productBundlingResult
import com.tokopedia.topchat.chatroom.view.activity.robot.productBundlingRobot
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
        stubIntents()

        // Then
        broadcastResult {
            assertBroadcastShown()
        }
        productBundlingResult {
            assertMultiBundlingBroadcastShown()
        }
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
        stubIntents()

        // Then
        broadcastResult {
            assertBroadcastShown()
        }
        productBundlingResult {
            assertCarouselBundlingBroadcastShown(2)
        }
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
        stubIntents()

        // Then
        broadcastResult {
            assertBroadcastShown()
        }
        productBundlingResult {
            assertSingleBundlingBroadcastShown()
        }
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
        stubIntents()

        // Then
        broadcastResult {
            assertBroadcastShown()
        }
        productBundlingResult {
            assertSingleBundlingBroadcastShown()
            assertCtaOutOfStock(false)
        }
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
        stubIntents()

        // When
        productBundlingRobot {
            doScrollProductBundlingToPositionInBroadcast(1)
            clickOnImageMultipleItemBundling()
        }

        // Then
        broadcastResult {
            assertBroadcastShown()
        }
        val intent = RouteManager.getIntent(context,
            "tokopedia://product/2149129024?extParam=whid%3D341730%26src%3Dchat") //Applink from JSON
        generalResult {
            openPageWithIntent(intent)
        }
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
        stubIntents()

        // When
        productBundlingRobot {
            clickOnImageSingleItemBundling()
        }

        // Then
        broadcastResult {
            assertBroadcastShown()
        }
        val intent = RouteManager.getIntent(context,
            "tokopedia://product/2149129024?extParam=whid%3D341730%26src%3Dchat") //Applink from JSON
        generalResult {
            openPageWithIntent(intent)
        }
    }
}
