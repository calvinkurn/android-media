package com.tokopedia.topchat.chatroom.view.activity.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.view.View
import androidx.test.espresso.Espresso.onView
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
import org.hamcrest.Matcher

open class BaseSellerTopchatRoomTest : TopchatRoomTest() {

    private val gson = Gson()

    protected lateinit var sellerProductChatReplies: GetExistingChatPojo
    protected lateinit var sellerProductAttachment: ChatAttachmentResponse

    override fun setupResponse() {
        super.setupResponse()
        sellerProductChatReplies = AndroidFileUtil.parse(
                "seller/success_get_chat_first_page_as_seller.json",
                GetExistingChatPojo::class.java
        )
        sellerProductAttachment = AndroidFileUtil.parse(
                "seller/success_get_chat_attachments_seller.json",
                ChatAttachmentResponse::class.java
        )
    }

    protected fun assertStockCountVisibilityAt(productCardPosition: Int, viewMatcher: Matcher<in View>) {
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        productCardPosition, R.id.tp_seller_stock_count
                )
        ).check(matches(viewMatcher))
    }

    protected fun assertStockCountValueAt(productCardPosition: Int, stockCount: Int) {
        onView(
                withRecyclerView(R.id.recycler_view).atPositionOnView(
                        productCardPosition, R.id.tp_seller_stock_count
                )
        ).check(matches(withText("$stockCount")))
    }

    protected fun createSuccessUpdateStockIntentResult(
            productId: String, stock: Int, status: ProductStatus
    ) {
        val intent = Intent().apply {
            putExtra(ProductManageCommonConstant.EXTRA_PRODUCT_ID, productId)
            putExtra(ProductManageCommonConstant.EXTRA_UPDATED_STOCK, stock)
            putExtra(ProductManageCommonConstant.EXTRA_UPDATED_STATUS, status.name)
        }
        intending(anyIntent()).respondWith(
                Instrumentation.ActivityResult(Activity.RESULT_OK, intent)
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