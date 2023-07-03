package com.tokopedia.inbox.universalinbox.util

import androidx.annotation.StringRes

interface UniversalInboxResourceProvider {
    fun getStringFromResource(@StringRes id: Int): String

    fun getSectionChatTitle(): String
    fun getSectionOthersTitle(): String

    fun getMenuChatBuyerTitle(): String
    fun getMenuChatSellerTitle(): String

    fun getMenuDiscussionTitle(): String
    fun getMenuReviewTitle(): String
}
