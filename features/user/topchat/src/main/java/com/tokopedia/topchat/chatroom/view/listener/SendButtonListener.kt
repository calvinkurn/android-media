package com.tokopedia.topchat.chatroom.view.listener

import androidx.fragment.app.FragmentManager

interface SendButtonListener {
    fun onSendClicked(message: String, generateStartTime: String)
    fun onEmptyProductPreview()
    fun getSupportChildFragmentManager(): FragmentManager
}
