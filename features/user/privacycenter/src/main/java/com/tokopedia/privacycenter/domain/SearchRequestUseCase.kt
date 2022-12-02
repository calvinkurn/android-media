package com.tokopedia.privacycenter.domain

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.privacycenter.data.GetRequestDetailResponse
import com.tokopedia.privacycenter.data.SearchRequestBody
import com.tokopedia.privacycenter.remote.GetCredentialsApi
import com.tokopedia.privacycenter.remote.HeaderUtils
import com.tokopedia.privacycenter.remote.OneTrustApi
import com.tokopedia.privacycenter.ui.dsar.DsarConstants
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
            if (it.accessToken.isNotEmpty()) {
                val bearerHeader = HeaderUtils.createHeader(token = it.accessToken)
                val searchResult = oneTrustApi.searchRequest(params, bearerHeader).body()
                if (searchResult?.results?.isNotEmpty() == true) {
                    val latestRequest = searchResult.results.first()
                    return oneTrustApi.getRequest(latestRequest.requestQueueRefId, bearerHeader).body() ?: GetRequestDetailResponse()
                } else {
                    return GetRequestDetailResponse()
                }
            }
        }
        throw MessageErrorException(DsarConstants.LABEL_ERROR_REQUEST)
    }

    override fun graphqlQuery(): String = ""
}
