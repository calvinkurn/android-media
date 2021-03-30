package com.tokopedia.topchat.chatroom.data.activityresult

import com.tokopedia.chat_common.data.ProductAttachmentViewModel

class UpdateProductStockResult(
        val product: ProductAttachmentViewModel,
        val lastKnownPosition: Int
)