package com.tokopedia.contactus.inboxtickets.view.listeners

import com.tokopedia.contactus.inboxtickets.domain.AttachmentItem

interface InboxDetailListener {
    fun onCommentClick(agreed: Boolean, commentPosition: Int, commentId: String)
    fun onPriorityLabelClick()
    fun onTransactionDetailsClick()
    fun showImageAttachment(position: Int,
                            imagesURL: List<AttachmentItem>)

    fun scrollTo(position: Int)
}

interface AttachmentListener{
    fun showImagePreview(position: Int,
                         imagesItems: List<AttachmentItem>)
}
