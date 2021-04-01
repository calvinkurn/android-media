package com.tokopedia.topchat.chatroom.view.activity

import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
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
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        1, R.id.tp_seller_stock_category
                )
        ).check(matches(isDisplayed()))
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        1, R.id.tp_seller_stock_category
                )
        ).check(matches(withText("Stok:")))
        assertStockCountVisibilityAt(1, isDisplayed())
        assertStockCountValueAt(1, 5)
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
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        1, R.id.tp_seller_stock_category
                )
        ).check(matches(isDisplayed()))
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        1, R.id.tp_seller_stock_category
                )
        ).check(matches(withText("Stok campaign:")))
        assertStockCountVisibilityAt(1, isDisplayed())
        assertStockCountValueAt(1, 5)
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
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        1, R.id.btn_update_stock
                )
        ).perform(click())

        // Then
        assertStockCountVisibilityAt(1, isDisplayed())
        assertStockCountValueAt(1, 55)
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
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        1, R.id.btn_update_stock
                )
        ).perform(click())

        // Then
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        1, R.id.lb_product_label
                )
        ).check(matches(isDisplayed()))
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        1, R.id.lb_product_label
                )
        ).check(matches(withText("Stok habis")))
        assertStockCountVisibilityAt(1, isDisplayed())
        assertStockCountValueAt(1, 0)
    }
}
