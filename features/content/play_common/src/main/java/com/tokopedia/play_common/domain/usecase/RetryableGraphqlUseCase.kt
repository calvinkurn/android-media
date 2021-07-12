package com.tokopedia.play_common.domain.usecase

import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.network.exception.MessageErrorException

/**
 * Created by jegul on 12/07/21
 */
abstract class RetryableGraphqlUseCase<T: Any>(
        graphqlRepository: GraphqlRepository,
        private val retryCount: Int = DEFAULT_RETRY_COUNT,
) : GraphqlUseCase<T>(graphqlRepository) {

    override suspend fun executeOnBackground(): T {
        var count = 0

        while(count <= retryCount) {
            try {
                val response = super.executeOnBackground()
                val isSuccess = isResponseSuccess(response)
                if (isSuccess) return response
                else error("Response is not success")
            } catch (e: Throwable) {
                count += 1
            }
        }

        throw MessageErrorException(getErrorMessage())
    }

    open fun isResponseSuccess(response: T): Boolean {
        return true
    }

    open fun getErrorMessage(): String {
        return DEFAULT_ERROR_MESSAGE
    }

    companion object {

        private const val DEFAULT_RETRY_COUNT = 3
        private const val DEFAULT_ERROR_MESSAGE = "Terjadi kesalahan pada sistem. Silahkan coba lagi."
    }
}