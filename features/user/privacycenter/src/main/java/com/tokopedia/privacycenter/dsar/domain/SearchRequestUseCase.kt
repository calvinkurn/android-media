package com.tokopedia.privacycenter.dsar.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.privacycenter.dsar.model.GetRequestDetailResponse
import com.tokopedia.privacycenter.dsar.model.SearchRequestBody
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class SearchRequestUseCase @Inject constructor(
    val userSession: UserSessionInterface,
    val oneTrustApi: OneTrustApi,
    val getCredentialsApi: GetCredentialsApi,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<SearchRequestBody, GetRequestDetailResponse>(dispatcher.io) {

    override suspend fun execute(params: SearchRequestBody): GetRequestDetailResponse {
        val credentials = getCredentialsApi.fetchCredential()
        credentials?.let {
            val bearerHeader = HeaderUtils.createHeader(token = it.accessToken)
            val searchResult = oneTrustApi.searchRequest(params, bearerHeader).body()
            if(searchResult?.results?.isNotEmpty() == true) {
                val latestRequest = searchResult.results.first()
                return oneTrustApi.getRequest(latestRequest.requestQueueRefId, bearerHeader).body() ?: GetRequestDetailResponse()
            }
        }
        return GetRequestDetailResponse()
    }

    override fun graphqlQuery(): String = ""
}
