package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import com.tokopedia.applink.RouteManager
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.TopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.openPageWithIntent
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
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        launchChatRoomActivity()

        // Then
        assertMultiBundlingShown()
    }

    @Test
    fun should_show_single_product_bundling_when_success_get_attachment() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentSingleChat
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        launchChatRoomActivity()

        // Then
        assertSingleBundlingShown()
    }

    @Test
    fun should_show_label_single_product_bundling_when_success_get_attachment() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentSingleChat
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        launchChatRoomActivity()

        // Then
        labelSingleBundlingShown("Paket isi 1")
    }

    @Test
    fun should_show_cta_product_bundling_when_user_is_buyer() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentMultipleChat
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        launchChatRoomActivity(isSellerApp = false)

        // Then
        assertCtaBundlingShown()
    }

    @Test
    fun should_not_show_cta_product_bundling_when_user_is_seller() {
        // Given
        getChatUseCase.response = getSwappedRolesChat()
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        launchChatRoomActivity(isSellerApp = true)

        // Then
        assertCtaBundlingNotShown()
    }

    @Test
    fun should_open_package_page_when_click_multi_bundling_button() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentMultipleChat
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        launchChatRoomActivity()

        //When
        Intents.intending(anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        doScrollChatToPosition(0)
        clickCtaProductBundling(0)

        // Then
        val intent = RouteManager.getIntent(
            context,
            "tokopedia://product-bundle/2148348897?source=cart&bundleId=32175&selectedProductIds=2148348892,2148348896,2148348901"
        )
        openPageWithIntent(intent)
    }

    @Test
    fun should_open_package_page_when_click_single_bundling_button() {
        // Given
        getChatUseCase.response = getChatUseCase.productBundlingAttachmentSingleChat
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
        launchChatRoomActivity()

        // When
        Intents.intending(anyIntent())
            .respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        doScrollChatToPosition(0)
        clickCtaProductBundling(0)

        //Then
        val intent = RouteManager.getIntent(
            context,
            "tokopedia://product-bundle/2148348897?source=cart&bundleId=32175&selectedProductIds=2148348892",
        )
        openPageWithIntent(intent)
    }

    @Test
    fun should_show_all_bundling_when_get_carousel_product_bundling() {
        // Given
        getChatUseCase.response = getCarouselProductBundling()
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
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
        chatAttachmentUseCase.response = chatAttachmentUseCase.productBundlingAttachment
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
            .attachment.apply {
                id = "1507930103"
                attributes = customCarouselProductBundling
            }
        return carouselBundling
    }

    private val customCarouselProductBundling = "{\"product_bundling\":[{\"bundle_id\":\"43\",\"bundle_type\":1,\"bundle_title\":\"Home Care Ramadhan\",\"bundle_status\":1,\"original_price\":\"Rp28.000\",\"original_price_float\":\"28000.0\",\"bundle_price\":\"Rp26.000\",\"bundle_price_float\":\"26000.0\",\"total_discount\":\"Rp2.000\",\"total_discount_float\":\"2000.0\",\"cta_bundling\":{\"android_link\":\"tokopedia://product-bundle/2148348897?source=cart&bundleId=32175&selectedProductIds=2148348892\",\"button_shown\":true,\"cta_text\":\"Lihat Paket\",\"desktop_link\":\"\",\"ios_link\":\"tokopedia://product-bundle/2148348897?source=cart&bundleId=32175&selectedProductIds=2148348892,2148348896,2148348901\",\"is_disabled\":false,\"mobile_link\":\"\"},\"bundle_item\": [{\"product_id\":\"2147811665\",\"name\":\"Test Carousel 1-1\",\"image_url\":\"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/5/20/083a171e-3282-45d9-a8ca-8b30ffb27695.jpg\",\"status\":\"1\",\"quantity\":\"1\",\"original_price\":\"Rp14.000\",\"original_price_float\":\"14000.0\",\"bundle_price\":\"Rp13.000\",\"bundle_price_float\":\"13000.0\"},{\"product_id\":\"2147811695\",\"name\":\"Test Carousel 1-2\",\"image_url\":\"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/5/20/083a171e-3282-45d9-a8ca-8b30ffb27695.jpg\",\"status\":\"1\",\"quantity\":\"1\",\"original_price\":\"Rp14.000\",\"original_price_float\":\"14000.0\",\"bundle_price\":\"Rp13.000\",\"bundle_price_float\":\"13000.0\"}]}, {\"bundle_id\":\"44\",\"bundle_type\":2,\"bundle_title\":\"Home Care Ramadhan\",\"bundle_status\":1,\"original_price\":\"Rp28.000\",\"original_price_float\":\"28000.0\",\"bundle_price\":\"Rp26.000\",\"bundle_price_float\":\"26000.0\",\"total_discount\":\"Rp2.000\",\"total_discount_float\":\"2000.0\",\"cta_bundling\":{\"android_link\":\"tokopedia://product-bundle/2148348897?source=cart&bundleId=32175&selectedProductIds=2148348892,2148348896,2148348901\",\"button_shown\":true,\"cta_text\":\"Lihat Paket\",\"desktop_link\":\"\",\"ios_link\":\"tokopedia://product-bundle/2148348897?source=cart&bundleId=32175&selectedProductIds=2148348892,2148348896,2148348901\",\"is_disabled\":false,\"mobile_link\":\"\"},\"bundle_item\": [{\"product_id\":\"2147811665\",\"name\":\"Test Carousel 2-1\",\"image_url\":\"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/5/20/083a171e-3282-45d9-a8ca-8b30ffb27695.jpg\",\"status\":\"1\",\"quantity\":\"1\",\"original_price\":\"Rp14.000\",\"original_price_float\":\"14000.0\",\"bundle_price\":\"Rp13.000\",\"bundle_price_float\":\"13000.0\"},{\"product_id\":\"2147811695\",\"name\":\"Test Carousel 2-2\",\"image_url\":\"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/5/20/083a171e-3282-45d9-a8ca-8b30ffb27695.jpg\",\"status\":\"1\",\"quantity\":\"1\",\"original_price\":\"Rp14.000\",\"original_price_float\":\"14000.0\",\"bundle_price\":\"Rp13.000\",\"bundle_price_float\":\"13000.0\"}]}, {\"bundle_id\":\"45\",\"bundle_type\":2,\"bundle_title\":\"Home Care Ramadhan\",\"bundle_status\":-1,\"original_price\":\"Rp28.000\",\"original_price_float\":\"28000.0\",\"bundle_price\":\"Rp26.000\",\"bundle_price_float\":\"26000.0\",\"total_discount\":\"Rp2.000\",\"total_discount_float\":\"2000.0\",\"cta_bundling\":{\"android_link\":\"tokopedia://product-bundle/2148348897?source=cart&bundleId=32175&selectedProductIds=2148348892,2148348896,2148348901\",\"button_shown\":true,\"cta_text\":\"Lihat Paket\",\"desktop_link\":\"\",\"ios_link\":\"tokopedia://product-bundle/2148348897?source=cart&bundleId=32175&selectedProductIds=2148348892,2148348896,2148348901\",\"is_disabled\":true,\"mobile_link\":\"\"},\"bundle_item\": [{\"product_id\":\"2147811665\",\"name\":\"Test Carousel 3-1\",\"image_url\":\"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/5/20/083a171e-3282-45d9-a8ca-8b30ffb27695.jpg\",\"status\":\"1\",\"quantity\":\"1\",\"original_price\":\"Rp14.000\",\"original_price_float\":\"14000.0\",\"bundle_price\":\"Rp13.000\",\"bundle_price_float\":\"13000.0\"},{\"product_id\":\"2147811695\",\"name\":\"Test Carousel 3-2\",\"image_url\":\"https://ecs7.tokopedia.net/img/cache/700/VqbcmM/2021/5/20/083a171e-3282-45d9-a8ca-8b30ffb27695.jpg\",\"status\":\"1\",\"quantity\":\"1\",\"original_price\":\"Rp14.000\",\"original_price_float\":\"14000.0\",\"bundle_price\":\"Rp13.000\",\"bundle_price_float\":\"13000.0\"}]}]}"
}