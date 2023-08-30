package com.tokopedia.topchat.chatroom.view.activity.test.buyer

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.product.detail.common.VariantPageSource
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaResult
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.previewAttachmentRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.productCardResult
import com.tokopedia.topchat.chatroom.view.activity.robot.productCardRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.productPreviewResult
import com.tokopedia.topchat.chatroom.view.activity.robot.productPreviewRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.productResult
import com.tokopedia.topchat.chatroom.view.activity.robot.srwResult
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.SOURCE_TOPCHAT
import com.tokopedia.topchat.matchers.withTotalItem
import org.hamcrest.CoreMatchers.not
import org.junit.Before
import org.junit.Test

@UiTest
class TopchatRoomBuyerProductAttachmentTest : BaseBuyerTopchatRoomTest() {

    @Before
    override fun before() {
        super.before()
        addToCartUseCase.isError = false
    }

    @Test
    fun user_can_see_preview_product_before_attach_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        productPreviewResult {
            assertAttachmentPreview(isDisplayed())
            assertAttachmentPreview(withTotalItem(1))
        }
    }

    @Test
    fun user_can_not_sent_preview_product_when_text_is_empty() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            clickSendBtn()
        }

        // Then
        productPreviewResult {
            assertAttachmentPreview(isDisplayed())
            assertAttachmentPreview(withTotalItem(1))
        }
        generalResult {
            assertToasterText(context.getString(R.string.topchat_desc_empty_text_box))
        }
    }

    @Test
    fun user_preview_product_from_attach_product_page() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    getAttachProductData(1)
                )
            )

        // When
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }

        // Then
        productPreviewResult {
            assertAttachmentPreview(isDisplayed())
            assertAttachmentPreview(withTotalItem(1))
        }
    }

    @Test
    fun user_reattach_product_from_preview_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    getAttachProductData(1)
                )
            )
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }

        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    getAttachProductData(2)
                )
            )
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }

        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    getAttachProductData(3)
                )
            )
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }
        previewAttachmentRobot {
            scrollToPosition(2)
        }

        // Then
        productPreviewResult {
            assertAttachmentPreview(isDisplayed())
            assertAttachmentPreview(withTotalItem(3))
        }
    }

    @Test
    fun user_remove_preview_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    getAttachProductData(1)
                )
            )

        // When
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }
        productPreviewRobot {
            clickCloseAttachmentPreview(0)
        }

        // Then
        productPreviewResult {
            assertAttachmentPreview(not(isDisplayed()))
        }
    }

    @Test
    fun user_remove_two_out_of_three_preview_products() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generate3PreAttachPayload()
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK,
                    getAttachProductData(3)
                )
            )

        // When
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }
        productPreviewRobot {
            clickCloseAttachmentPreview(0)
            clickCloseAttachmentPreview(1)
        }

        // Then
        productPreviewResult {
            assertAttachmentPreview(isDisplayed())
            assertAttachmentPreview(withTotalItem(1))
        }
    }

    @Test
    fun template_chat_shown_if_product_preview_is_closed() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // Then
        productPreviewRobot {
            clickCloseAttachmentPreview(0)
        }

        // Then
        srwResult {
            assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    @Test
    fun user_can_see_preview_product_variant() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(exProductId)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        generalRobot {
            scrollChatToPosition(0)
        }

        // Then
        productPreviewResult {
            verifyVariantLabel(R.id.tv_variant_color, isDisplayed(), 0)
            verifyVariantLabel(R.id.tv_variant_size, isDisplayed(), 0)
        }
    }

    @Test
    fun user_can_see_attached_product_variants() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        // When
        generalRobot {
            scrollChatToPosition(0)
        }

        // Then
        productCardResult {
            hasVariantLabel(R.id.tv_variant_color, testVariantColor, 1)
            hasVariantLabel(R.id.tv_variant_size, testVariantSize, 1)
        }
    }

    @Test
    fun should_directly_add_to_cart_when_click_keranjang_in_attached_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(4)
        }
        productCardRobot {
            clickATCButtonAt(4)
        }

        // Then
        generalResult {
            assertToasterText(context.getString(R.string.title_topchat_see_cart))
        }
    }

    @Test
    fun should_open_cart_when_click_beli_in_attached_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(4)
        }
        productCardRobot {
            clickBuyButtonAt(4)
        }

        // Then
        generalResult {
            openPageWithApplink(ApplinkConst.CART)
        }
    }

    @Test
    fun should_open_bottomsheet_when_click_beli_in_attached_product_variants() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(0)
        }
        productCardRobot {
            clickBuyButtonAt(1)
        }

        // Then
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.ATC_VARIANT,
            "1160424090",
            "6115659",
            VariantPageSource.TOPCHAT_PAGESOURCE.source,
            "false",
            ""
        ) // Product from firstPageChatAsBuyer
        generalResult {
            openPageWithIntent(intent)
        }
    }

    @Test
    fun should_open_bottomsheet_when_click_keranjang_in_attached_product_variants() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(0)
        }
        productCardRobot {
            clickBuyButtonAt(1)
        }

        // Then
        val intent = RouteManager.getIntent(
            context,
            ApplinkConstInternalMarketplace.ATC_VARIANT,
            "1160424090",
            "6115659",
            VariantPageSource.TOPCHAT_PAGESOURCE.source,
            "false",
            ""
        ) // Product from firstPageChatAsBuyer
        generalResult {
            openPageWithIntent(intent)
        }
    }

    @Test
    fun should_show_check_wishlist_and_success_toaster_wishlist_when_user_add_to_wishlist() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = getZeroStockAttachment()
        addToWishlistV2UseCase.isFail = false
        launchChatRoomActivity()
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(0)
        }
        productCardRobot {
            clickWishlistButtonAt(1) // click wishlist
            clickWishlistButtonAt(1) // click go to wishlist
        }

        // Then
        productCardResult {
            hasProductWishlistButtonWithText("Cek Wishlist Kamu", 1)
        }
        generalResult {
            assertToasterText(context.getString(com.tokopedia.wishlist_common.R.string.on_success_add_to_wishlist_msg))
        }
    }

    @Test
    fun should_show_error_toaster_wishlist_when_user_fail_to_add_to_wishlist() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = getZeroStockAttachment()
        addToWishlistV2UseCase.isFail = true
        launchChatRoomActivity()
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(0)
        }
        productCardRobot {
            clickWishlistButtonAt(1) // click wishlist
            clickWishlistButtonAt(1) // click go to wishlist
        }

        // Then
        generalResult {
            context.getString(com.tokopedia.wishlist_common.R.string.on_failed_add_to_wishlist_msg)
        }
    }

    @Test
    fun should_show_error_toaster_when_user_click_beli_but_error() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        addToCartUseCase.isError = true
        launchChatRoomActivity()
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(4)
        }
        productCardRobot {
            clickBuyButtonAt(4)
        }

        // Then
        generalResult {
            assertToasterWithSubText("Oops!")
        }
    }

    @Test
    fun should_show_error_toaster_when_user_click_beli_but_failed() {
        // Given
        val errorMsg = "Error Test"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        addToCartUseCase.errorMessage = arrayListOf(errorMsg)
        launchChatRoomActivity()
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(4)
        }
        productCardRobot {
            clickBuyButtonAt(4)
        }

        // Then
        generalResult {
            assertToasterWithSubText(errorMsg)
        }
    }

    @Test
    fun user_can_go_to_pdp_page_when_click_on_attached_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(1)
        }
        productCardRobot {
            clickProductAttachmentAt(1)
        }

        // Then
        generalResult {
            openPageWithApplink("tokopedia://product/2148833237?extParam=whid=341734&src=chat")
        }
    }

    @Test
    fun should_not_show_label_empty_and_remind_button_when_product_is_archived() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentUseCase.productArchivedAttachment
        launchChatRoomActivity()
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(1)
        }

        // Then
        productResult {
            hasProductName(1, "")
            hasProductPrice(1, "")
            hasNoVisibleEmptyStockLabelAt(1)
            hasNoVisibleRemindMeBtnAt(1)
        }
    }

    // TODO: assert attach product, stock info seller, and tokocabang is not displayed on buyer side

    override fun getAttachProductData(totalProduct: Int): Intent {
        val products = ArrayList<ResultProduct>(totalProduct)
        for (i in 0 until totalProduct) {
            products.add(
                ResultProduct(
                    i.toString(),
                    "tokopedia://product/1111",
                    ProductPreviewAttribute.productThumbnail,
                    "Rp ${i + 1}5.000.000",
                    "${i + 1} ${ProductPreviewAttribute.productName}"
                )
            )
        }
        return Intent().apply {
            putParcelableArrayListExtra(
                ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY,
                products
            )
        }
    }

    companion object {
        val testVariantSize = "S"
        val testVariantColor = "Putih"
        val exProductId = "1111"
        fun putProductAttachmentIntent(intent: Intent) {
            val productIds = listOf(exProductId)
            val stringProductPreviews = CommonUtil.toJson(productIds)
            intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
        }
    }

    private fun getZeroStockAttachment(): ChatAttachmentResponse {
        return AndroidFileUtil.parse(
            "success_get_chat_attachments_zero_stock.json",
            ChatAttachmentResponse::class.java
        )
    }
}
