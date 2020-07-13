package com.tokopedia.talk.feature.reply.presentation.widget.listeners

interface ThreadListener {
    fun onUserDetailsClicked(userId: String, isSeller: Boolean, shopdId: String)
    fun goToProfilePage(userId:String)
    fun onUrlClicked(link: String): Boolean
}