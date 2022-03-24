package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralRobot.doScrollChatToPosition
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertCarouselBundlingShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertCtaBundlingNotShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertCtaBundlingShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertCtaOutOfStock
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertMultiBundlingShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.assertSingleBundlingShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingResult.labelSingleBundlingShown
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingRobot.clickCtaProductBundling
import com.tokopedia.topchat.chatroom.view.activity.robot.product_bundling.ProductBundlingRobot.doScrollProductBundlingToPosition
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
    fun should_show_all_bundling_when_get_carousel_product_bundling() {
        // Given
        getChatUseCase.response = getCarouselProductBundling()
        launchChatRoomActivity()

        //When - Then
        doScrollChatToPosition(0)
        doScrollProductBundlingToPosition(2)
        assertCarouselBundlingShown(3)
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

    private fun getCarouselProductBundling(): GetExistingChatPojo {
        val carouselBundling = getChatUseCase.productBundlingAttachmentMultipleChat
        carouselBundling.chatReplies
            .list.first().chats.first().replies.first()
            .attachment.attributes = customCarouselProductBundling()
        return carouselBundling
    }

    private fun customCarouselProductBundling(): String {
        val bundleSingle = """
            {"bundle_id":"43","bundle_type":1,"bundle_title":"Home Care Ramadhan","bundle_status":1,"original_price":"Rp28.000","original_price_float":"28000.0","bundle_price":"Rp26.000","bundle_price_float":"26000.0","total_discount":"Rp2.000","total_discount_float":"2000.0","button_text": "Lihat Paket","button_desktop_link":"","button_mobile_link": "","button_android_link":"tokopedia://product-bundle/<selected_product_on_pdp>?bundleId=<bundle_id>&selectedProductIds=<selectedvariant>","button_ios_link":"tokopedia://product-bundle/<selected_product_on_pdp>?bundleId=<bundle_id>&selectedProductIds=<selectedvariant>","bundle_item": [{"productID":"2147811665","name":"test product 1","image_url":"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/5/20/083a171e-3282-45d9-a8ca-8b30ffb27695.jpg","status":"1","quantity":"1","original_price":"Rp14.000","original_price_float":"14000.0","bundle_price":"Rp13.000","bundle_price_float":"13000.0"},{"productID":"2147811695","name":"test product 2","image_url":"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/5/20/083a171e-3282-45d9-a8ca-8b30ffb27695.jpg","status":"1","quantity":"1","original_price":"Rp14.000","original_price_float":"14000.0","bundle_price":"Rp13.000","bundle_price_float":"13000.0"}]}
        """.trimIndent()
        val bundleMultiple = """
            {"bundle_id":"43","bundle_type":2,"bundle_title":"Home Care Ramadhan","bundle_status":1,"original_price":"Rp28.000","original_price_float":"28000.0","bundle_price":"Rp26.000","bundle_price_float":"26000.0","total_discount":"Rp2.000","total_discount_float":"2000.0","button_text": "Lihat Paket","button_desktop_link":"","button_mobile_link": "","button_android_link":"tokopedia://product-bundle/<selected_product_on_pdp>?bundleId=<bundle_id>&selectedProductIds=<selectedvariant>","button_ios_link":"tokopedia://product-bundle/<selected_product_on_pdp>?bundleId=<bundle_id>&selectedProductIds=<selectedvariant>","bundle_item": [{"productID":"2147811665","name":"test product 1","image_url":"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/5/20/083a171e-3282-45d9-a8ca-8b30ffb27695.jpg","status":"1","quantity":"1","original_price":"Rp14.000","original_price_float":"14000.0","bundle_price":"Rp13.000","bundle_price_float":"13000.0"},{"productID":"2147811695","name":"test product 2","image_url":"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/5/20/083a171e-3282-45d9-a8ca-8b30ffb27695.jpg","status":"1","quantity":"1","original_price":"Rp14.000","original_price_float":"14000.0","bundle_price":"Rp13.000","bundle_price_float":"13000.0"}]}
        """.trimIndent()
        val bundleOOS = """
            {"bundle_id":"43","bundle_type":2,"bundle_title":"Home Care Ramadhan","bundle_status":-1,"original_price":"Rp28.000","original_price_float":"28000.0","bundle_price":"Rp26.000","bundle_price_float":"26000.0","total_discount":"Rp2.000","total_discount_float":"2000.0","button_text": "Lihat Paket","button_desktop_link":"","button_mobile_link": "","button_android_link":"tokopedia://product-bundle/<selected_product_on_pdp>?bundleId=<bundle_id>&selectedProductIds=<selectedvariant>","button_ios_link":"tokopedia://product-bundle/<selected_product_on_pdp>?bundleId=<bundle_id>&selectedProductIds=<selectedvariant>","bundle_item": [{"productID":"2147811665","name":"test product 1","image_url":"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/5/20/083a171e-3282-45d9-a8ca-8b30ffb27695.jpg","status":"1","quantity":"1","original_price":"Rp14.000","original_price_float":"14000.0","bundle_price":"Rp13.000","bundle_price_float":"13000.0"},{"productID":"2147811695","name":"test product 2","image_url":"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/5/20/083a171e-3282-45d9-a8ca-8b30ffb27695.jpg","status":"1","quantity":"1","original_price":"Rp14.000","original_price_float":"14000.0","bundle_price":"Rp13.000","bundle_price_float":"13000.0"}]}
        """.trimIndent()
        return """
              {"product_bundling":[$bundleSingle, $bundleMultiple, $bundleOOS]}
        """.trimIndent()
    }
}