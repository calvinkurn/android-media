package com.tokopedia.topchat.common.util

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 27/01/21
 */

class TopChatSellerReviewHelper @Inject constructor(
        @ApplicationContext private val context: Context,
        private val userSession: UserSessionInterface
) {

    companion object {
        private const val PREFERENCE_NAME = "CACHE_SELLER_IN_APP_REVIEW"
        private const val KEY_CHATS_REPLIED_TO = "KEY_SIR_CHATS_REPLIED_TO"
    }

    var hasRepliedChat: Boolean = false

    fun saveMessageId(messageId: String) {
        try {
            if (!hasRepliedChat) return

            val sharedPref = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            val editor = sharedPref.edit()

            val messageIdsSet: MutableSet<String> = sharedPref.getStringSet(KEY_CHATS_REPLIED_TO + userSession.userId, mutableSetOf())
                    ?: mutableSetOf()
            val isMessageIdAlreadySaved = messageIdsSet.contains(messageId)
            val isQuotaFull = messageIdsSet.size >= 5

            if (isMessageIdAlreadySaved || isQuotaFull) {
                hasRepliedChat = false
                return
            }

            messageIdsSet.add(messageId)
            editor.putStringSet(KEY_CHATS_REPLIED_TO + userSession.userId, messageIdsSet)
            editor.apply()
        } catch (e: Exception) {
            Timber.w(e)
        }
    }
}