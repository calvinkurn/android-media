package com.tokopedia.topchat.chatroom.view.activity

import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
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
        setupChatRoomActivity()
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        inflateTestFragment()

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
        setupChatRoomActivity()
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        inflateTestFragment()

        // Then
        assertProductStockType(R.id.recycler_view, 1, isDisplayed())
        assertProductStockTypeText(R.id.recycler_view, 1, "Stok:")
        assertStockCountVisibilityAt(R.id.recycler_view, 1, isDisplayed())
        assertStockCountValueAt(R.id.recycler_view, 1, 5)
    }

    @Test
    fun assert_campaign_product_stock_info() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setCampaignStock(
                0, true
        )
        inflateTestFragment()

        // Then
        assertProductStockType(R.id.recycler_view, 1, isDisplayed())
        assertProductStockTypeText(R.id.recycler_view, 1, "Stok campaign:")
        assertStockCountVisibilityAt(R.id.recycler_view, 1, isDisplayed())
        assertStockCountValueAt(R.id.recycler_view, 1, 5)
    }

    @Test
    fun user_update_stock_single_product_active() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        createSuccessUpdateStockIntentResult(
                "1261590628", 55, ProductStatus.ACTIVE
        )
        inflateTestFragment()

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
        setupChatRoomActivity()
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        createSuccessUpdateStockIntentResult(
                "1261590628", 55, ProductStatus.ACTIVE, productName
        )
        inflateTestFragment()

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
        setupChatRoomActivity()
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        createSuccessUpdateStockIntentResult(
                "1261590628", 55, ProductStatus.ACTIVE, productName
        )
        inflateTestFragment()

        // When
        clickChangeStockBtn(R.id.recycler_view, 1)

        // Then
        assertSnackbarText("Stok produk \"$subProductName...\" berhasil diubah.")
    }

    @Test
    fun user_update_stock_single_product_inactive() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = sellerProductChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        createSuccessUpdateStockIntentResult(
                "1261590628", 55, ProductStatus.INACTIVE
        )
        inflateTestFragment()

        // When
        clickChangeStockBtn(R.id.recycler_view, 1)

        // Then
        assertLabelOnProductCard(R.id.recycler_view, 1, isDisplayed())
        assertLabelTextOnProductCard(R.id.recycler_view, 1, "Stok habis")
        assertStockCountVisibilityAt(R.id.recycler_view, 1, isDisplayed())
        assertStockCountValueAt(R.id.recycler_view, 1, 0)
    }

    @Test
    fun user_update_stock_normal_carousel_product_active() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = sellerProductCarouselChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        createSuccessUpdateStockIntentResult(
                "1261590628", 55, ProductStatus.ACTIVE
        )
        inflateTestFragment()

        // When
        clickChangeStockBtn(R.id.rv_product, 0)

        // Then
        assertStockCountVisibilityAt(R.id.rv_product, 0, isDisplayed())
        assertStockCountValueAt(R.id.rv_product, 0, 55)
    }

    @Test
    fun user_update_stock_normal_carousel_product_inactive() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = sellerProductCarouselChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        createSuccessUpdateStockIntentResult(
                "1261590628", 55, ProductStatus.INACTIVE
        )
        inflateTestFragment()

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
        setupChatRoomActivity()
        getChatUseCase.response = sellerBroadcastProductCarouselChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        createSuccessUpdateStockIntentResult(
                "1261590628", 55, ProductStatus.ACTIVE
        )
        inflateTestFragment()

        // When
        clickChangeStockBtn(R.id.rv_product_carousel, 0)

        // Then
        assertStockCountVisibilityAt(R.id.rv_product_carousel, 0, isDisplayed())
        assertStockCountValueAt(R.id.rv_product_carousel, 0, 55)
    }

    @Test
    fun user_update_stock_broadcast_carousel_product_inactive() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = sellerBroadcastProductCarouselChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment
        createSuccessUpdateStockIntentResult(
                "1261590628", 55, ProductStatus.INACTIVE
        )
        inflateTestFragment()

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
        setupChatRoomActivity()
        getChatUseCase.response = firstPageChatAsBuyer
        chatAttachmentUseCase.response = chatAttachmentResponse
        chatSrwUseCase.response = chatSrwResponse
        inflateTestFragment()
        intendingAttachProduct(1)

        // When
        clickPlusIconMenu()
        clickAttachProductMenu()

        // Then
        assertSrwContentIsHidden()
    }

    @Test
    fun update_stock_btn_is_hidden_if_stock_campaign() {
        // Given
        setupChatRoomActivity()
        getChatUseCase.response = sellerBroadcastProductCarouselChatReplies
        chatAttachmentUseCase.response = sellerProductAttachment.setCampaignStock(
                0, true
        )
        inflateTestFragment()

        // Then
        assertStockCountBtnVisibilityAt(
                R.id.rv_product_carousel, 0, not(isDisplayed())
        )
    }

    // TODO: assert toaster error from update product
}
