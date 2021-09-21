package com.tokopedia.topchat.chatroom.data.activityresult

import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer

/**
 * To hold data about current ongoing stock update of a product
 */
class UpdateProductStockResult(
        val product: ProductAttachmentViewModel,
        val lastKnownPosition: Int,
        val parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData?
)