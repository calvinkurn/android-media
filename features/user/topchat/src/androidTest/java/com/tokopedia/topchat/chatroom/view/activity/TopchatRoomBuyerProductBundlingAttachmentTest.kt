package com.tokopedia.topchat.chatroom.view.activity

import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertCtaBundlingNotShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertCtaBundlingShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertMultiBundlingShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertSingleBundlingShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.labelSingleBundlingShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingRobot.clickCtaProductBundling
import org.junit.Test

@UiTest
class TopchatRoomBuyerProductBundlingAttachmentTest : TopchatRoomTest() {
    @Test
    fun should_show_multiple_product_bundling_when_success_get_attachment() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentChat
        launchChatRoomActivity()

        // Then
        assertMultiBundlingShown(0)
    }

    @Test
    fun should_show_single_product_bundling_when_success_get_attachment() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentChat
        launchChatRoomActivity()

        // Then
        assertSingleBundlingShown(1)
    }

    @Test
    fun should_show_label_single_product_bundling_when_success_get_attachment() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentChat
        launchChatRoomActivity()

        // Then
        labelSingleBundlingShown(1, "Paket isi 1")
    }

    @Test
    fun should_show_cta_product_bundling_when_user_is_buyer() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentChat
        launchChatRoomActivity(isSellerApp = false)

        // Then
        assertCtaBundlingShown(0)
        assertCtaBundlingShown(1)
    }

    @Test
    fun should_not_show_cta_product_bundling_when_user_is_seller() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentChat
        launchChatRoomActivity(isSellerApp = true)

        // Then
        assertCtaBundlingNotShown(0)
        assertCtaBundlingNotShown(1)
    }

    @Test
    fun should_open_package_page_when_click_multi_bundling_button() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentChat
        launchChatRoomActivity()

        //When
        clickCtaProductBundling(0)

        // Then
    }

    @Test
    fun should_open_package_page_when_click_signle_bundling_button() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentChat
        launchChatRoomActivity()

        // When
        clickCtaProductBundling(1)

        //Then
    }
}