package com.tokopedia.talk.feature.reading.presentation.widget

interface ThreadListener {
    fun onThreadClicked(questionID: String)
    fun onUserDetailsClicked(userId: String)
    fun onLinkClicked(link: String) : Boolean
}