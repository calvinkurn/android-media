package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.data.model.GraphqlCacheStrategy
import com.tokopedia.graphql.data.model.GraphqlRequest
import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.play.broadcaster.util.error.DefaultErrorThrowable
import com.tokopedia.play.broadcaster.util.error.DefaultNetworkThrowable
import com.tokopedia.usecase.coroutines.UseCase
import java.net.UnknownHostException


/**
 * Created by mzennis on 30/06/20.
 */
abstract class BaseUseCase<out T : Any>: UseCase<T>() {

    suspend fun configureGqlResponse(
            gqlRepository: GraphqlRepository,
            gqlRequest: GraphqlRequest,
            gqlCacheStrategy: GraphqlCacheStrategy
    ): GraphqlResponse {
        try {
            return gqlRepository.getReseponse(listOf(gqlRequest), gqlCacheStrategy)
        } catch (throwable: Throwable) {
            if (throwable is UnknownHostException) throw DefaultNetworkThrowable()
        }
        throw DefaultErrorThrowable()
    }
}