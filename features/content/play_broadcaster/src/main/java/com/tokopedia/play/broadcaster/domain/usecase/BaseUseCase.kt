package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import com.tokopedia.play.broadcaster.util.error.DefaultNetworkThrowable
import com.tokopedia.usecase.coroutines.UseCase
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created by mzennis on 30/06/20.
 */
abstract class BaseUseCase<T : Any>: UseCase<T>() {

    suspend fun configureGqlResponse(
            gqlRepository: GraphqlRepository,
            query: String, typeOfT: Type, params: Map<String, Any>,
            gqlCacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        val gqlRequest = GraphqlRequest(query, typeOfT, params)
        var gqlResponse: GraphqlResponse? = null

        var retryCount = 0
        suspend fun executeWithRetry() {
            try {
                gqlResponse = gqlRepository.getReseponse(listOf(gqlRequest), gqlCacheStrategy)
            } catch (throwable: Throwable) {
                if (throwable is UnknownHostException || throwable is SocketTimeoutException) throw DefaultNetworkThrowable()
                else {
                    if (retryCount++ < MAX_RETRY) executeWithRetry()
                    else {
                        val errors = gqlResponse?.getError(typeOfT)
                        if (!errors.isNullOrEmpty()) {
                            if (GlobalConfig.DEBUG) {
                                throw DefaultErrorThrowable(errors[0].message)
                            }
                            // Crashlytics.log(0, TAG, errors[0].message)
                        }
                    }
                }
                // Crashlytics.log(0, TAG, throwable.localizedMessage) // TODO uncomment Crashlytics
            }
        }

        executeWithRetry()

        return gqlResponse?: throw DefaultErrorThrowable()
    }

    companion object {
        const val TAG = "play broadcaster"
        const val MAX_RETRY = 3
    }
}