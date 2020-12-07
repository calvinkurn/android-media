package com.tokopedia.play.broadcaster.util.handler

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import com.tokopedia.play.broadcaster.util.error.DefaultNetworkThrowable
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException


/**
 * Created by mzennis on 06/11/20.
 */
class DefaultUseCaseHandler(
        private val gqlRepository: GraphqlRepository,
        private val query: String,
        private val typeOfT: Type,
        private val params: Map<String, Any>,
        private val gqlCacheStrategy: GraphqlCacheStrategy
) : UseCaseHandler {

    override suspend fun executeWithRetry(): GraphqlResponse {
        val gqlRequest = GraphqlRequest(query, typeOfT, params)
        var gqlResponse: GraphqlResponse? = null

        var retryCount = 0
        suspend fun withRetry() {
            try {
                gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), gqlCacheStrategy)
            } catch (throwable: Throwable) {
                if (throwable is UnknownHostException || throwable is SocketTimeoutException) throw DefaultNetworkThrowable()
                else {
                    if (retryCount++ < MAX_RETRY) withRetry()
                }
            }
        }

        withRetry()

        val errors = gqlResponse?.getError(typeOfT)
        if (!errors.isNullOrEmpty()) {
            throw DefaultErrorThrowable(errors.first().message)
        }

        return gqlResponse?: throw DefaultErrorThrowable()
    }

    companion object {
        const val MAX_RETRY = 3
    }
}