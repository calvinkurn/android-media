package com.tokopedia.topchat.chatroom.view.activity

import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.product.manage.common.feature.variant.presentation.data.UpdateCampaignVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.activity.base.BaseSellerTopchatRoomTest
import com.tokopedia.topchat.matchers.withLinearLayoutGravity
import com.tokopedia.topchat.matchers.withRecyclerView
import org.hamcrest.CoreMatchers.not
import org.junit.Test

class TopchatRoomSellerProductAttachmentTest : BaseSellerTopchatRoomTest() {

    @Test
    fun assert_product_card_gravity() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()

        // Then
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        1, R.id.containerProductAttachment
                )
        ).check(matches(withLinearLayoutGravity(Gravity.END)))
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        1, R.id.ll_footer
                )
        ).check(matches(not(isDisplayed())))

        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        3, R.id.containerProductAttachment
                )
        ).check(matches(withLinearLayoutGravity(Gravity.START)))
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        3, R.id.ll_footer
                )
        ).check(matches(not(isDisplayed())))
    }

    @Test
    fun assert_normal_product_stock_info() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()

        // Then
        assertProductStockType(R.id.recycler_view, 1, isDisplayed())
        assertProductStockTypeText(R.id.recycler_view, 1, "Stok:")
        assertStockCountVisibilityAt(R.id.recycler_view, 1, isDisplayed())
        assertStockCountValueAt(R.id.recycler_view, 1, 5)
    }

    @Test
    fun assert_campaign_product_stock_info() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setCampaignStock(
                0, true
        )
        launchChatRoomActivity()

        // Then
        assertProductStockType(R.id.recycler_view, 1, isDisplayed())
        assertProductStockTypeText(R.id.recycler_view, 1, "Stok campaign:")
        assertStockCountVisibilityAt(R.id.recycler_view, 1, isDisplayed())
        assertStockCountValueAt(R.id.recycler_view, 1, 5)
    }

    @Test
    fun user_update_stock_single_product_active() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628", 55, ProductStatus.ACTIVE
        )

        // When
        clickChangeStockBtn(R.id.recycler_view, 1)

        // Then
        assertStockCountVisibilityAt(R.id.recycler_view, 1, isDisplayed())
        assertStockCountValueAt(R.id.recycler_view, 1, 55)
    }

    @Test
    fun user_update_stock_single_product_active_snackbar() {
        // Given
        val productName = "Product Testing"
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628", 55, ProductStatus.ACTIVE, productName
        )

        // When
        clickChangeStockBtn(R.id.recycler_view, 1)

        // Then
        assertSnackbarText("Stok produk \"$productName\" berhasil diubah.")
    }

    @Test
    fun user_update_stock_single_product_active_snackbar_trimmed() {
        // Given
        val productName = "Long product name testing"
        val subProductName = productName.substring(0, 20)
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628", 55, ProductStatus.ACTIVE, productName
        )

        // When
        clickChangeStockBtn(R.id.recycler_view, 1)

        // Then
        assertSnackbarText("Stok produk \"$subProductName...\" berhasil diubah.")
    }

    @Test
    fun user_update_stock_single_product_inactive() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628", 55, ProductStatus.INACTIVE
        )

        // When
        clickChangeStockBtn(R.id.recycler_view, 1)

        // Then
        assertLabelOnProductCard(R.id.recycler_view, 1, isDisplayed())
        assertEmptyStockLabelOnProductCard(R.id.recycler_view, 1)
        assertStockCountVisibilityAt(R.id.recycler_view, 1, isDisplayed())
        assertStockCountValueAt(R.id.recycler_view, 1, 0)
    }

    @Test
    fun user_update_stock_normal_carousel_product_active() {
        // Given
        getChatUseCase.response = sellerProductCarouselChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628", 55, ProductStatus.ACTIVE
        )

        // When
        clickChangeStockBtn(R.id.rv_product, 0)

        // Then
        assertStockCountVisibilityAt(R.id.rv_product, 0, isDisplayed())
        assertStockCountValueAt(R.id.rv_product, 0, 55)
    }

    @Test
    fun user_update_stock_normal_carousel_product_inactive() {
        // Given
        getChatUseCase.response = sellerProductCarouselChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628", 55, ProductStatus.INACTIVE
        )

        // When
        clickChangeStockBtn(R.id.rv_product, 0)

        // Then
        assertLabelOnProductCard(R.id.rv_product, 0, isDisplayed())
        assertLabelTextOnProductCard(R.id.rv_product, 0, "Stok habis")
        assertStockCountVisibilityAt(R.id.rv_product, 0, isDisplayed())
        assertStockCountValueAt(R.id.rv_product, 0, 0)
    }

    @Test
    fun user_update_stock_broadcast_carousel_product_active() {
        // Given
        getChatUseCase.response = sellerBroadcastProductCarouselChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628", 55, ProductStatus.ACTIVE
        )

        // When
        clickChangeStockBtn(R.id.rv_product_carousel, 0)

        // Then
        assertStockCountVisibilityAt(R.id.rv_product_carousel, 0, isDisplayed())
        assertStockCountValueAt(R.id.rv_product_carousel, 0, 55)
    }

    @Test
    fun user_update_stock_broadcast_carousel_product_inactive() {
        // Given
        getChatUseCase.response = sellerBroadcastProductCarouselChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628", 55, ProductStatus.INACTIVE
        )

        // When
        clickChangeStockBtn(R.id.rv_product_carousel, 0)

        // Then
        assertLabelOnProductCard(R.id.rv_product_carousel, 0, isDisplayed())
        assertLabelTextOnProductCard(R.id.rv_product_carousel, 0, "Stok habis")
        assertStockCountVisibilityAt(R.id.rv_product_carousel, 0, isDisplayed())
        assertStockCountValueAt(R.id.rv_product_carousel, 0, 0)
    }

    @Test
    fun srw_not_displayed_if_seller_attach_from_attach_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        launchChatRoomActivity()
        intendingAttachProduct(1)

        // When
        clickPlusIconMenu()
        clickAttachProductMenu()

        // Then
        assertSrwPreviewContentIsHidden()
        assertTemplateChatVisibility(isDisplayed())
    }

    @Test
    fun update_stock_btn_is_hidden_if_stock_campaign() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setCampaignStock(
                0, true
        )
        launchChatRoomActivity()

        // Then
        assertStockCountBtnVisibilityAt(
                R.id.recycler_view, 1, not(isDisplayed())
        )
    }

    @Test
    fun fail_update_stock_show_error_toaster() {
        // Given
        val errorMsg = "Gagal update stok."
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createErrorUpdateStockIntentResult(errorMsg)

        // When
        clickChangeStockBtn(R.id.recycler_view, 1)

        // Then
        assertSnackbarText(errorMsg)
    }

    @Test
    fun header_msg_from_smart_reply() {
        // Given
        getChatUseCase.response = sellerSmartReply
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()

        // Then
        assertHeaderRightMsgBubbleVisibility(0, isDisplayed())
        assertHeaderRightMsgBubbleBlueDotVisibility(0, isDisplayed())
        assertHeaderRightMsgBubbleText(0, "Dibalas oleh Smart Reply")
    }

    @Test
    fun header_msg_from_topbot() {
        // Given
        getChatUseCase.response = sellerTopBot
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()

        // Then
        assertHeaderRightMsgBubbleVisibility(0, isDisplayed())
        assertHeaderRightMsgBubbleBlueDotVisibility(0, isDisplayed())
        assertHeaderRightMsgBubbleText(0, "Dibalas oleh Smart Reply")
    }

    @Test
    fun header_msg_from_auto_reply() {
        // Given
        getChatUseCase.response = sellerAutoReply
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()

        // Then
        assertHeaderRightMsgBubbleVisibility(0, isDisplayed())
        assertHeaderRightMsgBubbleBlueDotVisibility(0, not(isDisplayed()))
        assertHeaderRightMsgBubbleText(0, "Dibalas oleh Balasan Otomatis")
    }

    @Test
    fun header_msg_from_normal_inbox() {
        // Given
        getChatUseCase.response = sellerSourceInbox
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()

        // Then
        assertHeaderRightMsgBubbleVisibility(0, not(isDisplayed()))
        assertHeaderRightMsgBubbleBlueDotVisibility(0, not(isDisplayed()))
    }

    @Test
    fun tokocabang_status_on_product_card() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setFulFillment(
                0, true
        )
        launchChatRoomActivity()

        // Then
        assertTokoCabangVisibility(
                R.id.recycler_view, 1, isDisplayed()
        )
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        1, R.id.tp_seller_fullfilment
                )
        ).check(matches(withText("Dilayani TokoCabang")))
    }

    @Test
    fun tokocabang_status_on_product_card_disabled() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setFulFillment(
                0, false
        )
        launchChatRoomActivity()

        // Then
        assertTokoCabangVisibility(
                R.id.recycler_view, 1, not(isDisplayed())
        )
    }

    @Test
    fun should_update_variant_stock_instead_of_main_stock() {
        // Given
        val productName = "Sepatu"
        val productId = "1261590628"
        val variantStockResult = 20
        val variantName = "Biru"
        val variantResult = UpdateCampaignVariantResult(
                ProductStatus.ACTIVE, variantStockResult, variantName
        )
        val variantMapResult = hashMapOf(
                productId to variantResult
        )
        getChatUseCase.response = sellerProductVariantChatReplies
        chatAttachmentUseCase.response = sellerProductVariantAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            productId, 55, ProductStatus.ACTIVE, productName, variantMapResult
        )

        // When
        clickChangeStockBtn(R.id.recycler_view, 1)

        // Then
        assertStockCountBtnVisibilityAt(
                R.id.recycler_view, 1, isDisplayed()
        )
        assertStockCountVisibilityAt(
                R.id.recycler_view, 1, isDisplayed()
        )
        assertStockCountValueAt(
                R.id.recycler_view, 1, variantStockResult
        )
        assertSnackbarText(
                "Stok produk \"$productName - $variantName\" berhasil diubah."
        )
    }

    @Test
    fun should_has_empty_stock_if_variant_result_is_inactive_from_update_stock() {
        // Given
        val productName = "Sepatu"
        val productId = "1261590628"
        val variantStockResult = 0
        val variantName = "Biru"
        val variantResult = UpdateCampaignVariantResult(
                ProductStatus.INACTIVE, variantStockResult, variantName
        )
        val variantMapResult = hashMapOf(
                productId to variantResult
        )
        getChatUseCase.response = sellerProductVariantChatReplies
        chatAttachmentUseCase.response = sellerProductVariantAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            productId, 55, ProductStatus.ACTIVE, productName, variantMapResult
        )

        // When
        clickChangeStockBtn(R.id.recycler_view, 1)

        // Then
        assertStockCountBtnVisibilityAt(
                R.id.recycler_view, 1, isDisplayed()
        )
        assertStockCountVisibilityAt(
                R.id.recycler_view, 1, isDisplayed()
        )
        assertStockCountValueAt(
                R.id.recycler_view, 1, 0
        )
        assertEmptyStockLabelOnProductCard(R.id.recycler_view, 1)
    }

    @Test
    fun should_show_deactivate_toaster_when_deactivate_product_from_update_stock() {
        // Given
        val productName = "Sepatu"
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setEmptyStock(
                0, false
        )
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628", 0, ProductStatus.INACTIVE, productName
        )

        // When
        clickChangeStockBtn(R.id.recycler_view, 1)

        // Then
        assertSnackbarText(
                "Produk \"$productName\" berhasil dinonaktifkan."
        )
    }

    @Test
    fun should_show_activate_toaster_when_activate_product_from_update_stock() {
        // Given
        val productName = "Sepatu"
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setEmptyStock(
                0, true
        )
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628", 20, ProductStatus.ACTIVE, productName
        )

        // When
        clickChangeStockBtn(R.id.recycler_view, 1)

        // Then
        assertSnackbarText(
                "Produk \"$productName\" berhasil diaktifkan."
        )
    }

    @Test
    fun should_has_empty_stock_with_parent_id() {
        // Given
        val productName = "Sepatu"
        val productId = "1830243767"
        val parentId = "1830243756"
        val variantStockResult = 0
        val variantName = "Biru"
        val variantResult = UpdateCampaignVariantResult(
                ProductStatus.ACTIVE, variantStockResult, variantName
        )
        val variantMapResult = hashMapOf(
                productId to variantResult
        )
        getChatUseCase.response = sellerProductVariantChatReplies
        chatAttachmentUseCase.response = sellerProductVariantAttachmentWithParentId
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            parentId, 55, ProductStatus.ACTIVE, productName, variantMapResult
        )

        // When
        clickChangeStockBtn(R.id.recycler_view, 1)

        // Then
        assertStockCountBtnVisibilityAt(
                R.id.recycler_view, 1, isDisplayed()
        )
        assertStockCountVisibilityAt(
                R.id.recycler_view, 1, isDisplayed()
        )
        assertStockCountValueAt(
                R.id.recycler_view, 1, 0
        )
    }

    @Test
    fun should_show_deactivate_product_variant_from_update_stock_without_changing_stock_with_parent_id() {
        // Given
        val productName = "Sepatu"
        val productId = "1830243767"
        val parentId = "1830243756"
        val originalVariantStock = 5
        val variantName = "Biru"
        val variantResult = UpdateCampaignVariantResult(
                ProductStatus.INACTIVE, originalVariantStock, variantName
        )
        val variantMapResult = hashMapOf(
                productId to variantResult
        )
        getChatUseCase.response = sellerProductVariantChatReplies
        chatAttachmentUseCase.response = sellerProductVariantAttachmentWithParentId
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            parentId, 5, ProductStatus.ACTIVE, productName, variantMapResult
        )

        // When
        clickChangeStockBtn(R.id.recycler_view, 1)

        // Then
        assertStockCountBtnVisibilityAt(
                R.id.recycler_view, 1, isDisplayed()
        )
        assertStockCountVisibilityAt(
                R.id.recycler_view, 1, isDisplayed()
        )
        assertStockCountValueAt(
                R.id.recycler_view, 1, 0
        )
        assertEmptyStockLabelOnProductCard(R.id.recycler_view, 1)
    }

}
