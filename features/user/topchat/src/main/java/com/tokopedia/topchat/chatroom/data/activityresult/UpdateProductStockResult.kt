package com.tokopedia.topchat.chatroom.data.activityresult

import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer

/**
 * To hold data about current ongoing stock update of a product
 */
class UpdateProductStockResult(
    val product: ProductAttachmentUiModel,
    val lastKnownPosition: Int,
    val parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData?
)