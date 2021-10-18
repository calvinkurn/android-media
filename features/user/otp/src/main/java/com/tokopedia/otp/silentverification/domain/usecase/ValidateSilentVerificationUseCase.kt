package com.tokopedia.otp.silentverification.domain.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.otp.silentverification.domain.model.ValidateSilentVerificationResponse
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

/**
 * Created by Yoris on 17/10/21.
 */

class ValidateSilentVerificationUseCase @Inject constructor(@ApplicationContext val repository: GraphqlRepository)
    : CoroutineUseCase<Map<String, String>, ValidateSilentVerificationResponse>(Dispatchers.IO) {

    override suspend fun execute(params: Map<String, String>): ValidateSilentVerificationResponse {
        return repository.request(graphqlQuery(), params)
    }

    override fun graphqlQuery(): String = ""

    companion object {
        const val PARAM_MSISDN = "msisdn"
        const val PARAM_ASSOCIATION_ID = "association_key"
    }
}