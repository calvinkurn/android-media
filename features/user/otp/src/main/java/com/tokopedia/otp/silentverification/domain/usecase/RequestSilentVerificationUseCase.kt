package com.tokopedia.otp.silentverification.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.otp.silentverification.domain.model.RequestSilentVerificationResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by Yoris on 17/10/21.
 */

class RequestSilentVerificationUseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository)
    : CoroutineUseCase<String, RequestSilentVerificationResponse>(Dispatchers.IO) {

    override suspend fun execute(params: String): RequestSilentVerificationResponse {
        return repository.request(graphqlQuery(), createParams(params))
    }

    private fun createParams(
        phoneNo: String
    ): Map<String, Any> = mapOf(
        PARAM_MSISDN to phoneNo
    )

    override fun graphqlQuery(): String = ""

    companion object {
        private const val PARAM_MSISDN = "msisdn"
    }
}