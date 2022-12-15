package com.tokopedia.talk.feature.reply.presentation.widget.listeners

interface OnReplyBottomSheetClickedListener {
    fun onReportOptionClicked(commentId: String)
    fun onBlockOptionClicked()
    fun onDeleteOptionClicked(commentId: String)
    fun onEditProductOptionClicked()
}
