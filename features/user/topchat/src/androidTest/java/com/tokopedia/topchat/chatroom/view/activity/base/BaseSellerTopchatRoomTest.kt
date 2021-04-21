package com.tokopedia.topchat.chatroom.view.activity.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.google.gson.Gson
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.productattachment.ProductAttachmentAttributes
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.matchers.withRecyclerView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matcher

open class BaseSellerTopchatRoomTest : TopchatRoomTest() {

    private val gson = Gson()

    protected var sellerSmartReply = GetExistingChatPojo()
    protected var sellerTopBot = GetExistingChatPojo()
    protected var sellerAutoReply = GetExistingChatPojo()
    protected var sellerSourceInbox = GetExistingChatPojo()
    protected var sellerProductChatReplies = GetExistingChatPojo()
    protected var sellerProductCarouselChatReplies = GetExistingChatPojo()
    protected var sellerBroadcastProductCarouselChatReplies = GetExistingChatPojo()
    protected var sellerProductAttachment = ChatAttachmentResponse()

    private val templateChats = listOf(
            "I am seller", "Yes, this product is ready"
    )

    @ExperimentalCoroutinesApi
    override fun before() {
        super.before()
        setupDefaultResponse()
    }

    private fun setupDefaultResponse() {
        chatSrwUseCase.response = chatSrwResponse
        getTemplateChatRoomUseCase.response = generateTemplateResponse(
                templates = templateChats
        )
    }

    override fun setupResponse() {
        super.setupResponse()
        sellerSmartReply = AndroidFileUtil.parse(
                "seller/success_chat_reply_smart_reply.json",
                GetExistingChatPojo::class.java
        )
        sellerTopBot = AndroidFileUtil.parse(
                "seller/success_chat_reply_topbot.json",
                GetExistingChatPojo::class.java
        )
        sellerAutoReply = AndroidFileUtil.parse(
                "seller/success_chat_reply_auto_reply.json",
                GetExistingChatPojo::class.java
        )
        sellerSourceInbox = AndroidFileUtil.parse(
                "seller/success_chat_reply_source_inbox.json",
                GetExistingChatPojo::class.java
        )
        sellerProductChatReplies = AndroidFileUtil.parse(
                "seller/success_get_chat_first_page_as_seller.json",
                GetExistingChatPojo::class.java
        )
        sellerProductCarouselChatReplies = AndroidFileUtil.parse(
                "seller/success_get_chat_first_page_product_carousel_as_seller.json",
                GetExistingChatPojo::class.java
        )
        sellerBroadcastProductCarouselChatReplies = AndroidFileUtil.parse(
                "seller/success_get_chat_first_page_broadcast_" +
                        "product_carousel_as_seller.json",
                GetExistingChatPojo::class.java
        )
        sellerProductAttachment = AndroidFileUtil.parse(
                "seller/success_get_chat_attachments_seller.json",
                ChatAttachmentResponse::class.java
        )
    }

    protected fun assertStockCountVisibilityAt(
            recyclerViewId: Int,
            productCardPosition: Int,
            viewMatcher: Matcher<in View>
    ) {
        onView(
                withRecyclerView(recyclerViewId).atPositionOnView(
                        productCardPosition, R.id.tp_seller_stock_count
                )
        ).check(matches(viewMatcher))
    }

    protected fun assertStockCountValueAt(
            recyclerViewId: Int,
            productCardPosition: Int,
            stockCount: Int
    ) {
        onView(
                withRecyclerView(recyclerViewId).atPositionOnView(
                        productCardPosition, R.id.tp_seller_stock_count
                )
        ).check(matches(withText("$stockCount")))
    }

    protected fun assertStockCountBtnVisibilityAt(
            recyclerViewId: Int,
            productCardPosition: Int,
            viewMatcher: Matcher<in View>
    ) {
        onView(
                withRecyclerView(recyclerViewId).atPositionOnView(
                        productCardPosition, R.id.btn_update_stock
                )
        ).check(matches(viewMatcher))
    }

    protected fun clickChangeStockBtn(
            recyclerViewId: Int,
            atPosition: Int
    ) {
        onView(
                withRecyclerView(recyclerViewId).atPositionOnView(
                        atPosition, R.id.btn_update_stock
                )
        ).perform(ViewActions.click())
    }

    protected fun createSuccessUpdateStockIntentResult(
            productId: String,
            stock: Int,
            status: ProductStatus,
            productName: String = "Product Testing"
    ) {
        val intent = Intent().apply {
            putExtra(ProductManageCommonConstant.EXTRA_PRODUCT_ID, productId)
            putExtra(ProductManageCommonConstant.EXTRA_UPDATED_STOCK, stock)
            putExtra(ProductManageCommonConstant.EXTRA_UPDATED_STATUS, status.name)
            putExtra(ProductManageCommonConstant.EXTRA_PRODUCT_NAME, productName)
        }
        intending(anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, intent)
        )
    }

    protected fun createErrorUpdateStockIntentResult(
            errorMsg: String
    ) {
        val intent = Intent().apply {
            putExtra(ProductManageCommonConstant.EXTRA_UPDATE_MESSAGE, errorMsg)
        }
        intending(anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_CANCELED, intent)
        )
    }

    protected fun ChatAttachmentResponse.setCampaignStock(
            attachmentIndex: Int,
            isCampaign: Boolean
    ): ChatAttachmentResponse {
        val attachment = chatAttachments.list[attachmentIndex]
        val product = gson.fromJson(
                attachment.attributes, ProductAttachmentAttributes::class.java
        )
        if (isCampaign) {
            product.productProfile.dropPercentage = "50"
            product.productProfile.priceBefore = "Rp 10.000.000"
        } else {
            product.productProfile.dropPercentage = ""
            product.productProfile.priceBefore = ""
        }
        attachment.attributes = gson.toJson(product)
        return this
    }
}