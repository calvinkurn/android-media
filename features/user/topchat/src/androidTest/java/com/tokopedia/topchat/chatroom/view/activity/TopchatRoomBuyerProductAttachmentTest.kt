package com.tokopedia.topchat.chatroom.view.activity

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
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
import com.tokopedia.topchat.matchers.withRecyclerView
import com.tokopedia.topchat.matchers.withTotalItem
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.topchat.AndroidFileUtil
import org.hamcrest.Matchers.not
import org.junit.Before
import org.junit.Test

@UiTest
class TopchatRoomBuyerProductAttachmentTest : BaseBuyerTopchatRoomTest() {

    lateinit var productPreview: ProductPreview
    private val testVariantSize = "S"
    private val testVariantColor = "Putih"

    @Before
    override fun before() {
        super.before()
        productPreview = ProductPreview(
            "1111",
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
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        //When
        scrollChatToPosition(0)

        // Then
        assertVariantProductPreview()
    }

    @Test
    fun user_can_see_attached_product_variants() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        //When
        scrollChatToPosition(0)

        // Then
        assertVariant()
    }

    // TODO: need to create fake usecase atcusecase to simulate success hit ATC?
//    @Test
//    fun should_directly_add_to_cart_when_click_keranjang_in_attached_product() {
//        // Given
//        getChatUseCase.response = firstPageChatAsBuyer
//        chatAttachmentUseCase.response = chatAttachmentResponse
//        launchChatRoomActivity()
//
//        //When
//        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
//        scrollChatToPosition(4)
//        onView(withRecyclerView(R.id.recycler_view_chatroom)
//            .atPositionOnView(4, R.id.tv_atc)).perform(click())
//
//        // Then
//        onView(withText(context.getString(R.string.title_topchat_see_cart)))
//            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
//    }

    // TODO: cannot validate applink on testapp if the destination page is not included/different module
//    @Test
//    fun should_open_cart_when_click_beli_in_attached_product() {
//        // Given
//        getChatUseCase.response = firstPageChatAsBuyer
//        chatAttachmentUseCase.response = chatAttachmentResponse
//        launchChatRoomActivity()
//
//        //When
//        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
//        scrollChatToPosition(4)
//        onView(withRecyclerView(R.id.recycler_view_chatroom)
//            .atPositionOnView(4, R.id.tv_buy)).perform(click())
//
//        // Then
//        intended(hasData(ApplinkConst.CART))
//    }

    @Test
    fun should_open_bottomsheet_when_click_beli_in_attached_product_variants() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        //When
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        scrollChatToPosition(0)
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(1, R.id.tv_buy)).perform(click())

        // Then
        val intent = RouteManager.getIntent(context,
            ApplinkConstInternalMarketplace.ATC_VARIANT,
            "1160424090", "6115659", AtcVariantHelper.TOPCHAT_PAGESOURCE, "false", "") //Product from firstPageChatAsBuyer
        intended(hasData(intent.data))
    }

    @Test
    fun should_open_bottomsheet_when_click_keranjang_in_attached_product_variants() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        launchChatRoomActivity()

        //When
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        scrollChatToPosition(0)
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(1, R.id.tv_atc)).perform(click())

        // Then
        val intent = RouteManager.getIntent(context,
            ApplinkConstInternalMarketplace.ATC_VARIANT,
            "1160424090", "6115659", AtcVariantHelper.TOPCHAT_PAGESOURCE, "false", "") //Product from firstPageChatAsBuyer
        intended(hasData(intent.data))
    }

    @Test
    fun should_show_toaster_when_user_click_ingatkan_saya() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = getZeroStockAttachment()
        launchChatRoomActivity()

        //When
        scrollChatToPosition(0)
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(1, R.id.tv_wishlist)).perform(click())

        // Then
        onView(withText(context.getString(R.string.title_topchat_success_atw)))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun should_show_error_toaster_when_user_click_ingatkan_saya_but_failed() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = getZeroStockAttachment()
        addWishListUseCase.isFail = true
        launchChatRoomActivity()

        //When
        scrollChatToPosition(0)
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(1, R.id.tv_wishlist)).perform(click())

        // Then
        onView(withText("Oops!"))
            .check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun should_open_wishlist_when_user_click_cek_wishlist() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = getZeroStockAttachment()
        launchChatRoomActivity()

        //When
        intending(anyIntent()).respondWith(Instrumentation.ActivityResult(Activity.RESULT_OK, null))
        scrollChatToPosition(0)
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(1, R.id.tv_wishlist)).perform(click())
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(1, R.id.tv_wishlist)).perform(click())

        //Then
        intended(hasData(ApplinkConst.NEW_WISHLIST))
    }

    // TODO: assert attach product, stock info seller, and tokocabang is not displayed on buyer side

    private fun putProductAttachmentIntent(intent: Intent) {
        val productPreviews = listOf(productPreview)
        val stringProductPreviews = CommonUtil.toJson(productPreviews)
        intent.putExtra(ApplinkConst.Chat.PRODUCT_PREVIEWS, stringProductPreviews)
    }

    private fun assertVariant() {
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(1, R.id.tv_variant_color))
            .check(matches(withText(testVariantColor)))
        onView(withRecyclerView(R.id.recycler_view_chatroom)
            .atPositionOnView(1, R.id.tv_variant_size))
            .check(matches(withText(testVariantSize)))
    }

    private fun assertVariantProductPreview() {
        onView(withRecyclerView(R.id.rv_attachment_preview)
            .atPositionOnView(0, R.id.tv_variant_color))
            .check(matches(withText(testVariantColor)))
        onView(withRecyclerView(R.id.rv_attachment_preview)
            .atPositionOnView(0, R.id.tv_variant_size))
            .check(matches(withText(testVariantSize)))
    }

    private fun getZeroStockAttachment(): ChatAttachmentResponse {
        return AndroidFileUtil.parse(
            "success_get_chat_attachments_zero_stock.json",
            ChatAttachmentResponse::class.java
        )
    }
}
