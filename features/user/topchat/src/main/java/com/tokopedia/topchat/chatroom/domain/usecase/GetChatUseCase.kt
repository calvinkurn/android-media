package com.tokopedia.topchat.chatroom.domain.usecase

import android.content.res.Resources
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.chat_common.domain.pojo.GetExistingChatPojo
import com.tokopedia.graphql.data.model.CacheType
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.graphql.data.source.cache.GraphqlCacheDataStore
import com.tokopedia.graphql.domain.GraphqlUseCase
import com.tokopedia.topchat.R
import rx.Subscriber
import javax.inject.Inject


/**
 * @author : Steven 30/11/18
 */

class GetChatUseCase @Inject constructor(
        val resources: Resources,
        private val graphqlUseCase: GraphqlUseCase,
        private val cacheDataStore: GraphqlCacheDataStore
) {

    fun execute(requestParams: Map<String, Any>, subscriber: Subscriber<GraphqlResponse>) {
        val query = GraphqlHelper.loadRawString(resources, R.raw.query_get_topchat_replies)
        val graphqlRequest = GraphqlRequest(query,
                GetExistingChatPojo::class.java, requestParams, false)

        val cacheStrategy = GraphqlCacheStrategy.Builder(CacheType.CLOUD_THEN_CACHE)
                .setSessionIncluded(true).build()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.setCacheStrategy(cacheStrategy)
        graphqlUseCase.execute(subscriber)
    }

    companion object {
        private const val PARAM_MESSAGE_ID: String = "msgId"
        private const val PARAM_BEFORE_REPLY_TIME: String = "beforeReplyTime"

        fun generateParam(messageId: String, beforeReplyTime: String): Map<String, Any> {
            val intMessageId = if (messageId.isNotBlank()) messageId.toInt() else 0
            return mapOf(
                    PARAM_MESSAGE_ID to intMessageId,
                    PARAM_BEFORE_REPLY_TIME to beforeReplyTime
            )
        }
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}