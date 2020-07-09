package com.tokopedia.talk.feature.reply.presentation.widget.listeners

interface ThreadListener {
    fun onUserDetailsClicked(userId: String)
    fun onUrlClicked(link: String): Boolean
}