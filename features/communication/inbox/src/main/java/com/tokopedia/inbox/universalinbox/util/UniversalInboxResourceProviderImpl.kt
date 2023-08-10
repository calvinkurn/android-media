package com.tokopedia.inbox.universalinbox.util

import android.content.Context
import androidx.annotation.StringRes
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.inbox.R
import timber.log.Timber
import javax.inject.Inject

class UniversalInboxResourceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
): UniversalInboxResourceProvider {

    override fun getStringFromResource(@StringRes id: Int): String {
        return try {
            context.getString(id)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            ""
        }
    }

    override fun getSectionChatTitle(): String {
        return getStringFromResource(R.string.universal_inbox_section_chat)
    }

    override fun getSectionOthersTitle(): String {
        return getStringFromResource(R.string.universal_inbox_section_others)
    }

    override fun getMenuChatBuyerTitle(): String {
        return getStringFromResource(R.string.universal_inbox_menu_chat_buyer)
    }

    override fun getMenuChatSellerTitle(): String {
        return getStringFromResource(R.string.universal_inbox_menu_chat_seller)
    }

    override fun getMenuDiscussionTitle(): String {
        return getStringFromResource(R.string.universal_inbox_menu_discussion)
    }

    override fun getMenuReviewTitle(): String {
        return getStringFromResource(R.string.universal_inbox_menu_review)
    }
}
