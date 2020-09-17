package com.tokopedia.talk.feature.reply.presentation.widget.listeners

interface ThreadListener {
    fun onUserDetailsClicked(userId: String, isSeller: Boolean, shopId: String)
    fun goToProfilePage(userId:String)
    fun onUrlClicked(link: String): Boolean
    fun onUnmaskCommentOptionSelected(commentId: String)
    fun onDismissUnmaskCard(commentId: String = "")
}