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
import java.lang.reflect.ParameterizedType
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
            val gqlResponse =  gqlRepository.getReseponse(listOf(gqlRequest), gqlCacheStrategy)
//            val errors = gqlResponse.getError(clazz)
//            if (!errors.isNullOrEmpty()) {
//                if (GlobalConfig.DEBUG) {
//                    throw DefaultErrorThrowable(errors[0].message)
//                }
//                Crashlytics.log(0, TAG, errors[0].message)
//            }
            return gqlResponse
        } catch (throwable: Throwable) {
            Crashlytics.log(0, TAG, throwable.localizedMessage)
            if (throwable is UnknownHostException) throw DefaultNetworkThrowable()
        }
        throw DefaultErrorThrowable()
    }

    companion object {
        const val TAG = "play broadcaster"
    }
}