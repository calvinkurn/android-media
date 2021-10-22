package com.tokopedia.topchat.chatroom.view.viewmodel

import com.tokopedia.attachcommon.data.ResultProduct
import com.tokopedia.chat_common.data.AttachmentType
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.data.SendableViewModel
import com.tokopedia.chat_common.data.attachment.AttachmentId
import com.tokopedia.attachcommon.preview.ProductPreview
import com.tokopedia.chat_common.domain.pojo.productattachment.FreeShipping
import com.tokopedia.chat_common.domain.pojo.roommetadata.RoomMetaData
import com.tokopedia.chat_common.util.IdentifierUtil
import com.tokopedia.chat_common.view.viewmodel.ChatRoomHeaderViewModel.Companion.ROLE_USER
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.topchat.chatroom.domain.usecase.TopChatWebSocketParam
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory.AttachmentPreviewFactory
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
        roomMetaData: RoomMetaData,
        message: String,
        userLocationInfo: LocalCacheModel,
        localId: String
    ): Any {
        val startTime = SendableViewModel.generateStartTime()
        val msgId = roomMetaData.msgId
        val toUid = roomMetaData.receiver.uid
        return TopChatWebSocketParam.generateParamSendProductAttachment(
            msgId, generateResultProduct(), startTime,
            toUid, productPreview, message, userLocationInfo,
            localId
        )
    }

    override fun generatePreviewMessage(
        roomMetaData: RoomMetaData,
        message: String
    ): SendableViewModel {
        return ProductAttachmentViewModel.Builder()
            .withRoomMetaData(roomMetaData)
            .withAttachmentId(AttachmentId.NOT_YET_GENERATED)
            .withAttachmentType(AttachmentType.Companion.TYPE_PRODUCT_ATTACHMENT)
            .withProductId(productPreview.id)
            .withProductName(productPreview.name)
            .withProductPrice(productPreview.price)
            .withProductUrl(productPreview.url)
            .withProductImage(productPreview.imageUrl)
            .withIsSender(true)
            .withMsg(message)
            .withCanShowFooter(canShowFooterProductAttachment(
                true, roomMetaData.sender.role
            ))
            .withPriceInt(productPreview.priceBeforeInt.toLong())
            .withDropPercentage(productPreview.dropPercentage)
            .withPriceBefore(productPreview.priceBefore)
            .withFreeShipping(FreeShipping(
                productPreview.productFsIsActive,
                productPreview.productFsImageUrl
            ))
            .withMinOrder(1)
            .withRemainingStock(productPreview.remainingStock)
            .withStatus(productPreview.status)
            .withImages(listOf(productPreview.imageUrl))
            .withNeedSync(false)
            .build()
    }

    private fun canShowFooterProductAttachment(isOpposite: Boolean, role: String): Boolean {
        return (!isOpposite && role.equals(ROLE_USER, ignoreCase = true))
                || (isOpposite && !role.equals(ROLE_USER, ignoreCase = true))
    }
}