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
                GetExistingChatPojo::class.java, requestParams)

        val cacheStrategy = GraphqlCacheStrategy.Builder(requestParams[PARAM_CACHE_STRATEGY] as CacheType?)
                .setSessionIncluded(true).build()
        graphqlUseCase.clearRequest()
        graphqlUseCase.addRequest(graphqlRequest)
        graphqlUseCase.setCacheStrategy(cacheStrategy)
        graphqlUseCase.execute(subscriber)
    }

    companion object {

        private val PARAM_MESSAGE_ID: String = "msgId"
        private val PARAM_PAGE: String = "page"
        private val PARAM_CACHE_STRATEGY: String = "cache"


        fun generateParamFirstTime(messageId: String): Map<String, Any> {
            val requestParams = HashMap<String, Any>()
            requestParams[PARAM_MESSAGE_ID] = if (messageId.isNotBlank()) messageId.toInt() else 0
            requestParams[PARAM_PAGE] = 1
            requestParams[PARAM_CACHE_STRATEGY] = CacheType.CLOUD_THEN_CACHE
            return requestParams
        }

        fun generateParam(messageId: String, page: Int): Map<String, Any> {
            val requestParams = HashMap<String, Any>()
            requestParams[PARAM_MESSAGE_ID] = if (messageId.isNotBlank()) messageId.toInt() else 0
            requestParams[PARAM_PAGE] = page
            requestParams[PARAM_CACHE_STRATEGY] = CacheType.CLOUD_THEN_CACHE
            return requestParams
        }
    }

    fun unsubscribe() {
        graphqlUseCase.unsubscribe()
    }

}