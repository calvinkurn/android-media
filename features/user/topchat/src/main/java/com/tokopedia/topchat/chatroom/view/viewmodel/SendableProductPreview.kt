package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.data.attachment.AttachmentId
import com.tokopedia.chat_common.data.preview.ProductPreview
import com.tokopedia.chat_common.domain.pojo.productattachment.FreeShipping
import com.tokopedia.chat_common.domain.pojo.productattachment.PlayStoreData
import com.tokopedia.chat_common.domain.pojo.productattachment.TopchatProductRating
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chat_common.util.IdentifierUtil
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel.Companion.ROLE_USER
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.domain.usecase.TopChatWebSocketParam
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
import com.tokopedia.utils.time.TimeHelper
import okhttp3.Interceptor

class SendableProductPreview(
    val productPreview: ProductPreview
) : SendablePreview {

    val localId = IdentifierUtil.generateLocalId()
    val productId get() = productPreview.id
    val productUrl get() = productPreview.url

    override fun type(attachmentPreviewFactory: AttachmentPreviewFactory): Int {
        return attachmentPreviewFactory.type(this)
    }

    override fun notEnoughRequiredData(): Boolean {
        return productPreview.notEnoughRequiredData()
    }

    fun hasVariant(): Boolean {
        return !doesNotHaveVariant()
    }

    fun doesNotHaveVariant(): Boolean {
        return productPreview.doesNotHaveVariant()
    }

    fun hasColorVariant(): Boolean = productPreview.hasColorVariant()

    fun hasSizeVariant(): Boolean = productPreview.hasSizeVariant()

    private fun generateResultProduct(): ResultProduct {
        return ResultProduct(
            productPreview.id,
            productPreview.url,
            productPreview.imageUrl,
            productPreview.price,
            productPreview.name
        )
    }

    override fun generateMsgObj(
        messageId: String,
        opponentId: String,
        message: String,
        listInterceptor: List<Interceptor>,
        userLocationInfo: LocalCacheModel,
        localId: String
    ): Any {
        val startTime = SendableViewModel.generateStartTime()
        return TopChatWebSocketParam.generateParamSendProductAttachment(
            messageId, generateResultProduct(), startTime,
            opponentId, productPreview, message, userLocationInfo,
            localId
        )
    }

    override fun generatePreviewMessage(
        roomMetaData: RoomMetaData,
        message: String
    ): SendableViewModel {
        return ProductAttachmentViewModel(
            messageId = roomMetaData.msgId,
            fromUid = roomMetaData.sender.uid,
            from = roomMetaData.sender.name,
            fromRole = roomMetaData.sender.name,
            attachmentId = AttachmentId.NOT_YET_GENERATED,
            attachmentType = AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT,
            replyTime = TimeHelper.getNowTimeStamp().toString(),
            isRead = false,
            productId = productPreview.id,
            productName = productPreview.name,
            productPrice = productPreview.price,
            productUrl = productPreview.url,
            productImage = productPreview.imageUrl,
            isSender = true,
            message = message,
            canShowFooter = canShowFooterProductAttachment(
                true, roomMetaData.sender.role
            ),
            blastId = 0,
            productPriceInt = productPreview.priceBeforeInt.toLong(),
            category = "",
            variants = listOf(),
            dropPercentage = productPreview.dropPercentage,
            priceBefore = productPreview.priceBefore,
            shopId = 0,
            freeShipping = FreeShipping(
                productPreview.productFsIsActive,
                productPreview.productFsImageUrl
            ),
            categoryId = 0,
            playStoreData = PlayStoreData(),
            minOrder = 1,
            remainingStock = productPreview.remainingStock,
            status = productPreview.status,
            wishList = false,
            images = listOf(productPreview.imageUrl),
            source = "",
            rating = TopchatProductRating(),
            replyId = "",
            localId = IdentifierUtil.generateLocalId()
        ).apply {
            finishLoading()
        }
    }

    private fun canShowFooterProductAttachment(isOpposite: Boolean, role: String): Boolean {
        return (!isOpposite && role.equals(ROLE_USER, ignoreCase = true))
                || (isOpposite && !role.equals(ROLE_USER, ignoreCase = true))
    }
}