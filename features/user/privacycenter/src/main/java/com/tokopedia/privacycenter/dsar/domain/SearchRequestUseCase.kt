package com.tokopedia.privacycenter.dsar.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.dsar.model.SearchRequestBody
import com.tokopedia.privacycenter.dsar.model.SearchRequestResponse
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class SearchRequestUseCase @Inject constructor(
    val userSession: UserSessionInterface,
    val oneTrustApi: OneTrustApi,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, SearchRequestResponse>(dispatcher.io) {

    override suspend fun execute(params: String): SearchRequestResponse {
        val token = oneTrustApi.getCredentials(
            "client_credentials".toRequestBody("text/plain".toMediaTypeOrNull()),
            "7505b9e763a44cd2b8a2c39072aba3a9".toRequestBody("text/plain".toMediaTypeOrNull()),
            "RKwtU9yHUEuQRiOhVp797cPZpy9zlKR9".toRequestBody("text/plain".toMediaTypeOrNull())
        ).body()?.accessToken ?: ""
        val createParam = SearchRequestBody(params)
        return oneTrustApi.searchRequest(createParam, HeaderUtils.createHeader(
            token = token,
        )).body() ?: SearchRequestResponse()
    }

    override fun graphqlQuery(): String = ""
}
