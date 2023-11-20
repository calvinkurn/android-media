package com.tokopedia.topchat.chatroom.view.activity.test.seller

import android.view.Gravity
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.tokopedia.product.manage.common.feature.variant.presentation.data.UpdateCampaignVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.test.application.annotations.UiTest
import com.tokopedia.topchat.chatroom.view.activity.base.BaseSellerTopchatRoomTest
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaResult
import com.tokopedia.topchat.chatroom.view.activity.robot.composeAreaRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.generalResult
import com.tokopedia.topchat.chatroom.view.activity.robot.generalRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.msgBubbleResult
import com.tokopedia.topchat.chatroom.view.activity.robot.productResult
import com.tokopedia.topchat.chatroom.view.activity.robot.productRobot
import com.tokopedia.topchat.chatroom.view.activity.robot.srwResult
import com.tokopedia.topchat.matchers.withLinearLayoutGravity
import org.hamcrest.CoreMatchers.not
import org.junit.Test

@UiTest
class TopchatRoomSellerProductAttachmentTest : BaseSellerTopchatRoomTest() {

    @Test
    fun assert_product_card_gravity() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()

        // Then
        productResult {
            assertContainerProductAttachment(1, withLinearLayoutGravity(Gravity.END))
            assertFooterProductAttachment(1, not(isDisplayed()))
            assertContainerProductAttachment(3, withLinearLayoutGravity(Gravity.START))
            assertFooterProductAttachment(3, not(isDisplayed()))
        }
    }

    @Test
    fun assert_normal_product_stock_info() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()

        // Then
        productResult {
            assertProductStockTypeAt(1, isDisplayed())
            assertProductStockTypeAt(1, withText("Stok:"))
            assertStockCountAt(1, isDisplayed())
            assertStockCountAt(1, withText("5"))
        }
    }

    @Test
    fun assert_campaign_product_stock_info() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setCampaignStock(
            0,
            true
        )
        launchChatRoomActivity()

        // Then
        productResult {
            assertProductStockTypeAt(1, isDisplayed())
            assertProductStockTypeAt(1, withText("Stok campaign:"))
            assertStockCountAt(1, isDisplayed())
            assertStockCountAt(1, withText("5"))
        }
    }

    @Test
    fun user_update_stock_single_product_active() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628",
            55,
            ProductStatus.ACTIVE
        )

        // When
        productRobot {
            clickChangeStockBtnAt(1)
        }

        // Then
        productResult {
            assertStockCountAt(1, isDisplayed())
            assertStockCountAt(1, withText("55"))
        }
    }

    @Test
    fun user_update_stock_single_product_active_snackbar() {
        // Given
        val productName = "Product Testing"
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628",
            55,
            ProductStatus.ACTIVE,
            productName
        )

        // When
        productRobot {
            clickChangeStockBtnAt(1)
        }

        // Then
        generalResult {
            assertToasterText("Stok produk \"$productName\" berhasil diubah.")
        }
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
            "1261590628",
            55,
            ProductStatus.ACTIVE,
            productName
        )

        // When
        productRobot {
            clickChangeStockBtnAt(1)
        }

        // Then
        generalResult {
            assertToasterText("Stok produk \"$subProductName...\" berhasil diubah.")
        }
    }

    @Test
    fun user_update_stock_single_product_inactive() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628",
            55,
            ProductStatus.INACTIVE
        )

        // When
        productRobot {
            clickChangeStockBtnAt(1)
        }

        // Then
        productResult {
            assertLabelAt(1, isDisplayed())
            assertLabelAt(1, withText("Stok habis"))
            assertStockCountAt(1, isDisplayed())
            assertStockCountAt(1, withText("0"))
        }
    }

    @Test
    fun user_update_stock_normal_carousel_product_active() {
        // Given
        getChatUseCase.response = sellerProductCarouselChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628",
            55,
            ProductStatus.ACTIVE
        )

        // When
        productRobot {
            clickChangeStockBtnOnCarouselAt(0)
        }

        // Then
        productResult {
            assertStockCountOnCarouselAt(0, isDisplayed())
            assertStockCountOnCarouselAt(0, withText("55"))
        }
    }

    @Test
    fun user_update_stock_normal_carousel_product_inactive() {
        // Given
        getChatUseCase.response = sellerProductCarouselChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628",
            55,
            ProductStatus.INACTIVE
        )

        // When
        productRobot {
            clickChangeStockBtnOnCarouselAt(0)
        }

        // Then
        productResult {
            assertLabelOnCarouselAt(0, isDisplayed())
            assertLabelOnCarouselAt(0, withText("Stok habis"))
            assertStockCountOnCarouselAt(0, isDisplayed())
            assertStockCountOnCarouselAt(0, withText("0"))
        }
    }

    @Test
    fun user_update_stock_broadcast_carousel_product_active() {
        // Given
        getChatUseCase.response = sellerBroadcastProductCarouselChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628",
            55,
            ProductStatus.ACTIVE
        )

        // When
        productRobot {
            clickChangeStockBtnOnCarouselBroadcastAt(0)
        }

        // Then
        productResult {
            assertStockCountOnCarouselBroadcastAt(0, isDisplayed())
            assertStockCountOnCarouselBroadcastAt(0, withText("55"))
        }
    }

    @Test
    fun user_update_stock_broadcast_carousel_product_inactive() {
        // Given
        getChatUseCase.response = sellerBroadcastProductCarouselChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628",
            55,
            ProductStatus.INACTIVE
        )

        // When
        productRobot {
            clickChangeStockBtnOnCarouselBroadcastAt(0)
        }

        // Then
        productResult {
            assertLabelOnCarouselBroadcastAt(0, isDisplayed())
            assertLabelOnCarouselBroadcastAt(0, withText("Stok habis"))
            assertStockCountOnCarouselBroadcastAt(0, isDisplayed())
            assertStockCountOnCarouselBroadcastAt(0, withText("0"))
        }
    }

    @Test
    fun srw_not_displayed_if_seller_attach_from_attach_product() {
        // Given
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        getTemplateChatRoomUseCase.response = getTemplateChatRoomUseCase.getTemplateResponseBuyer(true)
        launchChatRoomActivity()
        intendingAttachProduct(1)

        // When
        Thread.sleep(1000)
        composeAreaRobot {
            clickPlusIconMenu()
            clickAttachProductMenu()
        }

        // Then
        srwResult {
            assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        }
        srwResult {
            assertSrwPreviewContentContainerVisibility(not(isDisplayed()))
        }
        composeAreaResult {
            assertTemplateChatVisibility(isDisplayed())
        }
    }

    @Test
    fun update_stock_btn_is_hidden_if_stock_campaign() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setCampaignStock(
            0,
            true
        )
        launchChatRoomActivity()

        // Then
        productResult {
            assertStockCountBtnAt(1, not(isDisplayed()))
        }
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
        productRobot {
            clickChangeStockBtnAt(1)
        }

        // Then
        generalResult {
            assertToasterText(errorMsg)
        }
    }

    @Test
    fun header_msg_from_smart_reply() {
        // Given
        getChatUseCase.response = sellerSmartReply
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()

        // Then
        msgBubbleResult {
            assertHeaderRightMsgBubbleAt(0, isDisplayed())
            assertHeaderRightMsgBubbleBlueDotAt(0, isDisplayed())
            assertHeaderRightMsgBubbleAt(0, withText("Dibalas oleh Smart Reply"))
        }
    }

    @Test
    fun header_msg_from_topbot() {
        // Given
        getChatUseCase.response = sellerTopBot
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()

        // Then
        msgBubbleResult {
            assertHeaderRightMsgBubbleAt(0, isDisplayed())
            assertHeaderRightMsgBubbleBlueDotAt(0, isDisplayed())
            assertHeaderRightMsgBubbleAt(0, withText("Dibalas oleh Smart Reply"))
        }
    }

    @Test
    fun header_msg_from_auto_reply() {
        // Given
        getChatUseCase.response = sellerAutoReply
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()

        // Then
        msgBubbleResult {
            assertHeaderRightMsgBubbleAt(0, isDisplayed())
            assertHeaderRightMsgBubbleBlueDotAt(0, not(isDisplayed()))
            assertHeaderRightMsgBubbleAt(0, withText("Dibalas oleh Balasan Otomatis"))
        }
    }

    @Test
    fun header_msg_from_normal_inbox() {
        // Given
        getChatUseCase.response = sellerSourceInbox
        chatAttachmentUseCase.response = sellerProductAttachment
        launchChatRoomActivity()

        // Then
        msgBubbleResult {
            assertHeaderRightMsgBubbleAt(0, not(isDisplayed()))
            assertHeaderRightMsgBubbleBlueDotAt(0, not(isDisplayed()))
        }
    }

    @Test
    fun tokocabang_status_on_product_card() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setFulFillment(
            0,
            true
        )
        launchChatRoomActivity()

        // Then
        productResult {
            assertTokoCabangAt(1, isDisplayed())
        }
    }

    @Test
    fun tokocabang_status_on_product_card_disabled() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setFulFillment(
            0,
            false
        )
        launchChatRoomActivity()

        // Then
        productResult {
            assertTokoCabangAt(1, not(isDisplayed()))
        }
    }

    @Test
    fun should_update_variant_stock_instead_of_main_stock() {
        // Given
        val productName = "Sepatu"
        val productId = "1261590628"
        val variantStockResult = 20
        val variantName = "Biru"
        val variantResult = UpdateCampaignVariantResult(
            ProductStatus.ACTIVE,
            variantStockResult,
            variantName
        )
        val variantMapResult = hashMapOf(
            productId to variantResult
        )
        getChatUseCase.response = sellerProductVariantChatReplies
        chatAttachmentUseCase.response = sellerProductVariantAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            productId,
            55,
            ProductStatus.ACTIVE,
            productName,
            variantMapResult
        )

        // When
        productRobot {
            clickChangeStockBtnAt(1)
        }

        // Then
        productResult {
            assertStockCountBtnAt(1, isDisplayed())
            assertStockCountAt(1, isDisplayed())
            assertStockCountAt(1, withText("$variantStockResult"))
        }
        generalResult {
            assertToasterText("Stok produk \"$productName - $variantName\" berhasil diubah.")
        }
    }

    @Test
    fun should_has_empty_stock_if_variant_result_is_inactive_from_update_stock() {
        // Given
        val productName = "Sepatu"
        val productId = "1261590628"
        val variantStockResult = 0
        val variantName = "Biru"
        val variantResult = UpdateCampaignVariantResult(
            ProductStatus.INACTIVE,
            variantStockResult,
            variantName
        )
        val variantMapResult = hashMapOf(
            productId to variantResult
        )
        getChatUseCase.response = sellerProductVariantChatReplies
        chatAttachmentUseCase.response = sellerProductVariantAttachment
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            productId,
            55,
            ProductStatus.ACTIVE,
            productName,
            variantMapResult
        )

        // When
        productRobot {
            clickChangeStockBtnAt(1)
        }

        // Then
        productResult {
            assertStockCountBtnAt(1, isDisplayed())
            assertStockCountAt(1, isDisplayed())
            assertStockCountAt(1, withText("0"))
            assertLabelAt(1, withText("Stok habis"))
        }
    }

    @Test
    fun should_show_deactivate_toaster_when_deactivate_product_from_update_stock() {
        // Given
        val productName = "Sepatu"
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setEmptyStock(
            0,
            false
        )
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628",
            0,
            ProductStatus.INACTIVE,
            productName
        )

        // When
        productRobot {
            clickChangeStockBtnAt(1)
        }

        // Then
        generalResult {
            assertToasterText("Produk \"$productName\" berhasil dinonaktifkan.")
        }
    }

    @Test
    fun should_show_activate_toaster_when_activate_product_from_update_stock() {
        // Given
        val productName = "Sepatu"
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setEmptyStock(
            0,
            true
        )
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            "1261590628",
            20,
            ProductStatus.ACTIVE,
            productName
        )

        // When
        productRobot {
            clickChangeStockBtnAt(1)
        }

        // Then
        generalResult {
            assertToasterText("Produk \"$productName\" berhasil diaktifkan.")
        }
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
            ProductStatus.ACTIVE,
            variantStockResult,
            variantName
        )
        val variantMapResult = hashMapOf(
            productId to variantResult
        )
        getChatUseCase.response = sellerProductVariantChatReplies
        chatAttachmentUseCase.response = sellerProductVariantAttachmentWithParentId
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            parentId,
            55,
            ProductStatus.ACTIVE,
            productName,
            variantMapResult
        )

        // When
        productRobot {
            clickChangeStockBtnAt(1)
        }

        // Then
        productResult {
            assertStockCountBtnAt(1, isDisplayed())
            assertStockCountAt(1, isDisplayed())
            assertStockCountAt(1, withText("0"))
        }
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
            ProductStatus.INACTIVE,
            originalVariantStock,
            variantName
        )
        val variantMapResult = hashMapOf(
            productId to variantResult
        )
        getChatUseCase.response = sellerProductVariantChatReplies
        chatAttachmentUseCase.response = sellerProductVariantAttachmentWithParentId
        launchChatRoomActivity()
        createSuccessUpdateStockIntentResult(
            parentId,
            5,
            ProductStatus.ACTIVE,
            productName,
            variantMapResult
        )

        // When
        productRobot {
            clickChangeStockBtnAt(1)
        }

        // Then
        productResult {
            assertStockCountBtnAt(1, isDisplayed())
            assertStockCountAt(1, isDisplayed())
            assertStockCountAt(1, withText("0"))
            assertLabelAt(1, withText("Stok habis"))
        }
    }

    @Test
    fun should_not_show_label_empty_stock_button_update_stock_and_text_stock_when_product_is_archived() {
        // Given
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = chatAttachmentUseCase.productArchivedAttachmentSeller
        launchChatRoomActivity()
        stubIntents()

        // When
        generalRobot {
            scrollChatToPosition(1)
        }
        productRobot {
            clickProductAttachmentAt(1)
        }

        // Then
        productResult {
            hasProductName(1, "")
            hasProductPrice(1, "")
            hasNoVisibleEmptyStockLabelAt(1)
            hasNoVisibleRemindMeBtnAt(1)
        }
    }

    @Test
    fun seller_can_sent_preview_single_product() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generatePreAttachPayload(EX_PRODUCT_ID)
        launchChatRoomActivity {
            putProductAttachmentIntent(it)
        }

        // When
        composeAreaRobot {
            typeMessageComposeArea("Hi barang ini ready?")
            clickSendBtn()
        }

        // Then
        productResult {
            assertProductPreviewAttachmentAtPosition(position = 1)
            assertProductStockTypeAt(
                position = 1,
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)
            )
        }
    }

    @Test
    fun seller_can_sent_preview_double_product() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generate2PreAttachPayload()
        launchChatRoomActivity {
            putProductAttachmentIntent(
                it,
                listOf("2495612915", "4533627959")
            )
        }

        // When
        composeAreaRobot {
            typeMessageComposeArea("Hi barang ini ready?")
            clickSendBtn()
        }

        // Then
        productResult {
            assertProductCarouselWithTotal(position = 1, total = 2)
            assertStockCountBtnOnCarouselAt(
                position = 0, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            assertStockCountBtnOnCarouselAt(
                position = 1, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        }
    }

    @Test
    fun seller_can_sent_preview_triple_product() {
        // Given
        getChatUseCase.response = firstPageChatAsSeller
        chatAttachmentUseCase.response = chatAttachmentResponse
        getChatPreAttachPayloadUseCase.response = getChatPreAttachPayloadUseCase
            .generate3PreAttachPayload()
        launchChatRoomActivity {
            putProductAttachmentIntent(
                it,
                listOf("2495612915", "4533627959", "1988283205")
            )
        }

        // When
        composeAreaRobot {
            typeMessageComposeArea("Hi barang ini ready?")
            clickSendBtn()
        }

        // Then
        productResult {
            assertProductCarouselWithTotal(position = 1, total = 3)
            assertStockCountBtnOnCarouselAt(
                position = 0, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            assertStockCountBtnOnCarouselAt(
                position = 1, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
            assertStockCountBtnOnCarouselAt(
                position = 2, withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE))
        }
    }
}
