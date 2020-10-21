package com.tokopedia.inbox.view.ext

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tokopedia.inbox.R
import com.tokopedia.inbox.common.InboxFragmentType
import java.lang.UnsupportedOperationException

fun BottomNavigationView.setSelectedPage(@InboxFragmentType page: Int) {
    val pageId = when (page) {
//        InboxFragmentType.NOTIFICATION -> something
        InboxFragmentType.CHAT -> R.id.menu_inbox_chat
//        InboxFragmentType.DISCUSSION -> something
        else -> throw UnsupportedOperationException("Unsupported fragment type")
    }
    selectedItemId = pageId
}