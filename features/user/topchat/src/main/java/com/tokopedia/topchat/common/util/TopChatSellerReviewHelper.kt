package com.tokopedia.topchat.common.util

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.topchat.chatroom.di.SellerAppReviewCacheHandler
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 27/01/21
 */

class TopChatSellerReviewHelper @Inject constructor(
        @SellerAppReviewCacheHandler private val cacheHandler: LocalCacheHandler,
        private val userSession: UserSessionInterface
) {

    var hasRepliedChat: Boolean = false

    fun saveMessageId(messageId: String) {
        try {
            if (!hasRepliedChat) return

            val messageIdsSet: MutableSet<String> = cacheHandler.getStringSet(TkpdCache.SellerInAppReview.KEY_CHATS_REPLIED_TO + userSession.userId, mutableSetOf())
            val isMessageIdAlreadySaved = messageIdsSet.contains(messageId)
            val isQuotaFull = messageIdsSet.size >= 5

            if (isMessageIdAlreadySaved || isQuotaFull) {
                hasRepliedChat = false
                return
            }

            messageIdsSet.add(messageId)
            cacheHandler.putStringSet(TkpdCache.SellerInAppReview.KEY_CHATS_REPLIED_TO + userSession.userId, messageIdsSet)
            cacheHandler.applyEditor()
        } catch (e: Exception) {
            Timber.w(e)
        }
    }
}