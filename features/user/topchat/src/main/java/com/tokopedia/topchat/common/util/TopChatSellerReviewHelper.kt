package com.tokopedia.topchat.common.util

import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.abstraction.constant.TkpdCache
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.topchat.chatroom.di.SellerAppReviewCacheHandler
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created By @ilhamsuaib on 27/01/21
 */

class TopChatSellerReviewHelper @Inject constructor(
        @SellerAppReviewCacheHandler private val cacheHandler: LocalCacheHandler,
        private val userSession: UserSessionInterface
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    var hasRepliedChat: Boolean = false

    fun saveMessageId(messageId: String) {
        launchCatchError(block = {
            if (!hasRepliedChat) return@launchCatchError

            val messageIdsSet: MutableSet<String> = cacheHandler.getStringSet(TkpdCache.SellerInAppReview.KEY_CHATS_REPLIED_TO + userSession.userId, mutableSetOf())
            val isMessageIdAlreadySaved = messageIdsSet.contains(messageId)
            val isQuotaFull = messageIdsSet.size >= 5

            if (isMessageIdAlreadySaved || isQuotaFull) {
                hasRepliedChat = false
                return@launchCatchError
            }

            messageIdsSet.add(messageId)
            cacheHandler.putStringSet(TkpdCache.SellerInAppReview.KEY_CHATS_REPLIED_TO + userSession.userId, messageIdsSet)
            cacheHandler.applyEditor()
        }, onError = {
            Timber.w(it)
        })
    }
}