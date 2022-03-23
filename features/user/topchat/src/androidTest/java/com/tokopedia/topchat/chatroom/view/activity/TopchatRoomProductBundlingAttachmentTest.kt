package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralRobot.doScrollChatToPosition
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertCtaBundlingNotShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertCtaBundlingShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertCtaOutOfStock
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertMultiBundlingShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertSingleBundlingShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.labelSingleBundlingShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingRobot.clickCtaProductBundling
import org.junit.Test

@UiTest
class TopchatRoomProductBundlingAttachmentTest : TopchatRoomTest() {

    @Test
    fun should_show_multiple_product_bundling_when_success_get_attachment() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentMultipleChat
        launchChatRoomActivity()

        // Then
        assertMultiBundlingShown()
    }

    @Test
    fun should_show_single_product_bundling_when_success_get_attachment() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentSingleChat
        launchChatRoomActivity()

        // Then
        assertSingleBundlingShown()
    }

    @Test
    fun should_show_label_single_product_bundling_when_success_get_attachment() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentSingleChat
        launchChatRoomActivity()

        // Then
        labelSingleBundlingShown("Paket isi 1")
    }

    @Test
    fun should_show_cta_product_bundling_when_user_is_buyer() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentMultipleChat
        launchChatRoomActivity(isSellerApp = false)

        // Then
        assertCtaBundlingShown()
    }

    @Test
    fun should_not_show_cta_product_bundling_when_user_is_seller() {
        // Given
        getChatUseCase.response = getSwappedRolesChat()
        launchChatRoomActivity(isSellerApp = true)

        // Then
        assertCtaBundlingNotShown()
    }

    @Test
    fun should_open_package_page_when_click_multi_bundling_button() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentMultipleChat
        launchChatRoomActivity()

        //When
        Intents.intending(anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        doScrollChatToPosition(0)
        clickCtaProductBundling(0)

        // Then
    }

    @Test
    fun should_open_package_page_when_click_single_bundling_button() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentSingleChat
        launchChatRoomActivity()

        // When
        Intents.intending(anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        doScrollChatToPosition(0)
        clickCtaProductBundling(0)

        //Then
    }

    @Test
    fun should_show_disabled_button_when_product_bundling_out_of_stock() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentOOSChat
        launchChatRoomActivity()

        // Then
        doScrollChatToPosition(0)
        assertCtaOutOfStock()
    }

    private fun getSwappedRolesChat(): GetExistingChatPojo {
        val swappedChat = getChatUseCase.productBundlingAttachmentMultipleChat
        swappedChat.chatReplies.contacts.forEach {
            if (!it.isInterlocutor) {
                it.role = "Shop Owner"
            } else {
                it.role = "User"
            }
        }
        return swappedChat
    }
}