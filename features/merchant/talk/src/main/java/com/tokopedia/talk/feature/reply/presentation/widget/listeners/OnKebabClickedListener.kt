package com.tokopedia.talk.feature.reply.presentation.widget.listeners

interface OnKebabClickedListener {
    fun onKebabClicked(
        commentId: String,
        allowReport: Boolean,
        allowDelete: Boolean,
        allowBlock: Boolean
    )
}
