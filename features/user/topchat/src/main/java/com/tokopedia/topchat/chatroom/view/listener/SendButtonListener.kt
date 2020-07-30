package com.tokopedia.topchat.chatroom.view.listener

import androidx.fragment.app.FragmentManager

interface SendButtonListener {
    fun onEmptyProductPreview()
    fun getSupportChildFragmentManager(): FragmentManager
}
