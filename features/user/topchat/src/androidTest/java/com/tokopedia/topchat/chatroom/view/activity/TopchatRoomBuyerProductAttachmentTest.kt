package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.*
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.ApplinkConst.AttachProduct.TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.attachcommon.preview.ProductPreview
import com.tokopedia.common.network.util.CommonUtil
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chatroom.view.activity.base.BaseBuyerTopchatRoomTest
import com.tokopedia.topchat.common.TopChatInternalRouter.Companion.SOURCE_TOPCHAT
import com.tokopedia.topchat.matchers.withTotalItem
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.openPageWithApplink
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralResult.openPageWithIntent
import com.tokopedia.topchat.chatroom.view.activity.robot.general.GeneralRobot.doScrollChatToPosition
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardResult.hasFailedToasterWithMsg
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardResult.hasToasterWithMsg
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardResult.hasVariantLabel
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardRobot.clickATCButtonAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardRobot.clickBuyButtonAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductCardRobot.clickWishlistButtonAt
import com.tokopedia.topchat.chatroom.view.activity.robot.product.ProductPreviewResult.verifyVariantLabel
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test

@UiTest
class TopchatRoomBuyerProductAttachmentTest : BaseBuyerTopchatRoomTest() {

    lateinit var productPreview: ProductPreview
    private val testVariantSize = "S"
    private val testVariantColor = "Putih"
    private val exProductId = "1111"

    @Before
    override fun before() {
        super.before()
        productPreview = ProductPreview(
            exProductId,
            ProductPreviewAttribute.productThumbnail,
            ProductPreviewAttribute.productName,
            "Rp 23.000.000",
            "",
            testVariantColor,
            "",
            "",
            testVariantSize,
            "tokopedia://product/1111",
            false,
            "",
            "Rp 50.000.000",
            500000.0,
            "50%",
            false
        )
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
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview))
            .check(matches(withTotalItem(1)))
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
        clickSendBtn()

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview))
            .check(matches(withTotalItem(1)))
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
                    Activity.RESULT_OK, getAttachProductData(1)
                )
            )

        // When
        clickPlusIconMenu()
        clickAttachProductMenu()

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview))
            .check(matches(withTotalItem(1)))
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
                    Activity.RESULT_OK, getAttachProductData(1)
                )
            )
        clickPlusIconMenu()
        clickAttachProductMenu()
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK, getAttachProductData(2)
                )
            )
        clickPlusIconMenu()
        clickAttachProductMenu()
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK, getAttachProductData(3)
                )
            )
        clickPlusIconMenu()
        clickAttachProductMenu()
        onView(withId(R.id.rv_attachment_preview)).perform(
            scrollToPosition<RecyclerView.ViewHolder>(2)
        )

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview))
            .check(matches(withTotalItem(3)))
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
                    Activity.RESULT_OK, getAttachProductData(1)
                )
            )

        // When
        clickPlusIconMenu()
        clickAttachProductMenu()
        clickCloseAttachmentPreview(0)

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(not(isDisplayed())))
    }

    @Test
    fun user_remove_two_out_of_three_preview_products() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()
        intending(hasExtra(TOKOPEDIA_ATTACH_PRODUCT_SOURCE_KEY, SOURCE_TOPCHAT))
            .respondWith(
                Instrumentation.ActivityResult(
                    Activity.RESULT_OK, getAttachProductData(3)
                )
            )

        // When
        clickPlusIconMenu()
        clickAttachProductMenu()
        clickCloseAttachmentPreview(0)
        clickCloseAttachmentPreview(1)

        // Then
        onView(withId(R.id.rv_attachment_preview)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_attachment_preview))
            .check(matches(withTotalItem(1)))
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
        clickCloseAttachmentPreview(0)

        // Then
        assertSrwPreviewContentIsHidden()
        assertTemplateChatVisibility(isDisplayed())
    }

    @Test
    fun user_can_see_preview_product_variant() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase.
                generatePreAttachPayload(exProductId)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        //When
        doScrollChatToPosition(0)

        // Then
        verifyVariantLabel(R.id.tv_variant_color, isDisplayed(), 0)
        verifyVariantLabel(R.id.tv_variant_size, isDisplayed(), 0)
    }

    @Test
    fun user_can_see_attached_product_variants() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        //When
        doScrollChatToPosition(0)

        // Then
        hasVariantLabel(R.id.tv_variant_color, testVariantColor, 1)
        hasVariantLabel(R.id.tv_variant_size, testVariantSize, 1)
    }

    @Test
    fun should_directly_add_to_cart_when_click_keranjang_in_attached_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        //When
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        doScrollChatToPosition(4)
        clickATCButtonAt(4)

        // Then
        hasToasterWithMsg(context.getString(R.string.title_topchat_see_cart))
    }

    @Test
    fun should_open_cart_when_click_beli_in_attached_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        //When
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        doScrollChatToPosition(4)
        clickBuyButtonAt(4)

        // Then
        openPageWithApplink(ApplinkConst.CART)
    }

    @Test
    fun should_open_bottomsheet_when_click_beli_in_attached_product_variants() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        //When
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        doScrollChatToPosition(0)
        clickBuyButtonAt(1)

        // Then
        val intent = RouteManager.getIntent(context,
            ApplinkConstInternalMarketplace.ATC_VARIANT,
            "1160424090", "6115659", AtcVariantHelper.TOPCHAT_PAGESOURCE, "false", "") //Product from firstPageChatAsBuyer
        openPageWithIntent(intent)
    }

    @Test
    fun should_open_bottomsheet_when_click_keranjang_in_attached_product_variants() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        //When
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        doScrollChatToPosition(0)
        clickATCButtonAt(1)

        // Then
        val intent = RouteManager.getIntent(context,
            ApplinkConstInternalMarketplace.ATC_VARIANT,
            "1160424090", "6115659", AtcVariantHelper.TOPCHAT_PAGESOURCE, "false", "") //Product from firstPageChatAsBuyer
        openPageWithIntent(intent)
    }

    @Test
    fun should_show_toaster_when_user_click_ingatkan_saya() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = getZeroStockAttachment()
        addWishListUseCase.isFail = false
        launchChatRoomActivity()

        //When
        doScrollChatToPosition(0)
        clickWishlistButtonAt(1)

        // Then
        hasToasterWithMsg(context.getString(R.string.title_topchat_success_atw))
    }

    @Test
    fun should_show_error_toaster_when_user_click_ingatkan_saya_but_failed() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = getZeroStockAttachment()
        addWishListUseCase.isFail = true
        launchChatRoomActivity()

        //When
        doScrollChatToPosition(0)
        clickWishlistButtonAt(1)

        // Then
        hasFailedToasterWithMsg("Oops!")
    }

    @Test
    fun should_open_wishlist_when_user_click_cek_wishlist() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = getZeroStockAttachment()
        launchChatRoomActivity()

        //When
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        doScrollChatToPosition(0)
        clickWishlistButtonAt(1) //click wishlist
        clickWishlistButtonAt(1) //click go to wishlist

        //Then
        intended(hasData(ApplinkConst.NEW_WISHLIST))
    }

    @Test
    fun should_show_error_toaster_when_user_click_beli_but_error() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        addToCartUseCase.isError = true
        launchChatRoomActivity()

        //When
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        doScrollChatToPosition(4)
        clickBuyButtonAt(4)

        // Then
        hasFailedToasterWithMsg("Oops!")
    }

    @Test
    fun should_show_error_toaster_when_user_click_beli_but_failed() {
        // Given
        val errorMsg = "Error Test"
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        addToCartUseCase.errorMessage = arrayListOf(errorMsg)
        launchChatRoomActivity()

        //When
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        doScrollChatToPosition(4)
        clickBuyButtonAt(4)

        // Then
        hasFailedToasterWithMsg(errorMsg)
    }

    // TODO: assert attach product, stock info seller, and tokocabang is not displayed on buyer side

    private fun putProductAttachmentIntent(intent: Intent) {
        val productPreviews = listOf(productPreview)
        val stringProductPreviews = CommonUtil.toJson(productPreviews)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }

    private fun getZeroStockAttachment(): ChatAttachmentResponse {
        return AndroidFileUtil.parse(
            "success_get_chat_attachments_zero_stock.json",
            ChatAttachmentResponse::class.java
        )
    }
}
