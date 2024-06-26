package com.tokopedia.topchat.chatroom.view.activity.base

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import com.google.gson.Gson
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.chat_common.domain.pojo.productattachment.ProductAttachmentAttributes
import com.tokopedia.product.manage.common.feature.list.constant.ProductManageCommonConstant
import com.tokopedia.product.manage.common.feature.variant.presentation.data.UpdateCampaignVariantResult
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.topchat.AndroidFileUtil
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ChatAttachmentResponse
import com.tokopedia.topchat.chattemplate.domain.pojo.GetChatTemplate
import com.tokopedia.topchat.chattemplate.domain.pojo.GetChatTemplateResponse
import com.tokopedia.topchat.chattemplate.domain.pojo.TopchatChatTemplates
import com.tokopedia.websocket.WebSocketResponse

open class BaseSellerTopchatRoomTest : TopchatRoomTest() {

    private val gson = Gson()

    protected var sellerSmartReply = GetExistingChatPojo()
    protected var sellerTopBot = GetExistingChatPojo()
    protected var sellerAutoReply = GetExistingChatPojo()
    protected var sellerSourceInbox = GetExistingChatPojo()
    protected var sellerProductChatReplies = GetExistingChatPojo()
    protected var sellerProductVariantChatReplies = GetExistingChatPojo()
    protected var sellerProductCarouselChatReplies = GetExistingChatPojo()
    protected var sellerBroadcastProductCarouselChatReplies = GetExistingChatPojo()
    protected var sellerProductAttachment = ChatAttachmentResponse()
    protected var sellerProductVariantAttachment = ChatAttachmentResponse()
    protected var sellerProductVariantAttachmentWithParentId = ChatAttachmentResponse()
    protected var wsBuyerProductResponse: WebSocketResponse = WebSocketResponse()

    private val templateChats = arrayListOf(
        "I am seller",
        "Yes, this product is ready"
    )

    override fun before() {
        super.before()
        setupDefaultResponse()
    }

    private fun setupDefaultResponse() {
        chatSrwUseCase.response = chatSrwResponse
        getTemplateChatRoomUseCase.response = GetChatTemplateResponse(
            GetChatTemplate(
                sellerTemplate = TopchatChatTemplates(
                    templates = templateChats
                )
            )
        )
        wsBuyerProductResponse = AndroidFileUtil.parse(
            "ws/buyer/ws_buyer_attach_product.json",
            WebSocketResponse::class.java
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
        sellerProductVariantChatReplies = AndroidFileUtil.parse(
            "seller/get_chatReply_with_product_variant.json",
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
        sellerProductVariantAttachment = AndroidFileUtil.parse(
            "seller/success_get_chat_attachments_with_product_variant.json",
            ChatAttachmentResponse::class.java
        )
        sellerProductVariantAttachmentWithParentId = AndroidFileUtil.parse(
            "seller/success_get_chat_attachments_with_product_variant_parentid.json",
            ChatAttachmentResponse::class.java
        )
    }

    protected fun createSuccessUpdateStockIntentResult(
        productId: String,
        stock: Int,
        status: ProductStatus,
        productName: String = "Product Testing",
        variantMap: HashMap<String, UpdateCampaignVariantResult>? = null
    ) {
        val intent = Intent().apply {
            putExtra(ProductManageCommonConstant.EXTRA_PRODUCT_ID, productId)
            putExtra(ProductManageCommonConstant.EXTRA_UPDATED_STOCK, stock)
            putExtra(ProductManageCommonConstant.EXTRA_UPDATED_STATUS, status.name)
            putExtra(ProductManageCommonConstant.EXTRA_PRODUCT_NAME, productName)
            putExtra(ProductManageCommonConstant.EXTRA_UPDATE_VARIANTS_MAP, variantMap)
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
            attachment.attributes,
            ProductAttachmentAttributes::class.java
        )
        if (isCampaign) {
            product.productProfile.campaignId = "1000"
            product.productProfile.dropPercentage = "50"
            product.productProfile.priceBefore = "Rp 10.000.000"
        } else {
            product.productProfile.dropPercentage = ""
            product.productProfile.priceBefore = ""
            product.productProfile.campaignId = "0"
        }
        attachment.attributes = gson.toJson(product)
        return this
    }

    protected fun ChatAttachmentResponse.setEmptyStock(
        attachmentIndex: Int,
        isEmpty: Boolean
    ): ChatAttachmentResponse {
        val attachment = chatAttachments.list[attachmentIndex]
        val product = gson.fromJson(
            attachment.attributes,
            ProductAttachmentAttributes::class.java
        )
        if (isEmpty) {
            product.productProfile.status = ProductAttachmentUiModel.statusWarehouse
            product.productProfile.remainingStock = 0
        } else {
            product.productProfile.status = ProductAttachmentUiModel.statusActive
            product.productProfile.remainingStock = 1
        }
        attachment.attributes = gson.toJson(product)
        return this
    }

    protected fun ChatAttachmentResponse.setFulFillment(
        attachmentIndex: Int,
        isFulFillment: Boolean
    ): ChatAttachmentResponse {
        val attachment = chatAttachments.list[attachmentIndex]
        val product = gson.fromJson(
            attachment.attributes,
            ProductAttachmentAttributes::class.java
        )
        if (product.productProfile.isFulFillment != isFulFillment) {
            product.productProfile.isFulFillment = isFulFillment
        }
        attachment.attributes = gson.toJson(product)
        return this
    }
}
