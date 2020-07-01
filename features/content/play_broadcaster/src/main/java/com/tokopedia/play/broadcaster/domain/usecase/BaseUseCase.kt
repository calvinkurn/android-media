package com.tokopedia.play.broadcaster.domain.usecase

import com.crashlytics.android.Crashlytics
import com.tokopedia.config.GlobalConfig
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import com.tokopedia.play.broadcaster.util.error.DefaultNetworkThrowable
import com.tokopedia.usecase.coroutines.UseCase
import java.lang.reflect.Type
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
        try {
            gqlResponse =  gqlRepository.getReseponse(listOf(gqlRequest), gqlCacheStrategy)
        } catch (throwable: Throwable) {
            if (throwable is UnknownHostException) throw DefaultNetworkThrowable()
        }
        val errors = gqlResponse?.getError(typeOfT)
        if (!errors.isNullOrEmpty()) {
            if (GlobalConfig.DEBUG) {
                throw DefaultErrorThrowable(errors[0].message)
            }
        }
        return gqlResponse?: throw DefaultErrorThrowable()
    }

    companion object {
        const val TAG = "play broadcaster"
    }
}