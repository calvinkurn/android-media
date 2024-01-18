package com.tokopedia.shareexperience.data.usecase.shortlink

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shareexperience.data.dto.affiliate.generatelink.ShareExGenerateAffiliateLinkWrapperResponseDto
import com.tokopedia.shareexperience.data.query.ShareExGetAffiliateLinkQuery
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.asFlowResult
import com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate.ShareExAffiliateLinkPropertiesWrapperRequest
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetAffiliateLinkUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ShareExGetAffiliateLinkUseCaseImpl @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers
) : ShareExGetAffiliateLinkUseCase {

    private val query = ShareExGetAffiliateLinkQuery()

    override suspend fun getLink(
        params: ShareExAffiliateLinkPropertiesWrapperRequest
    ): Flow<ShareExResult<String>> {
        return flow {
            val response = repository.request<ShareExAffiliateLinkPropertiesWrapperRequest, ShareExGenerateAffiliateLinkWrapperResponseDto>(
                query,
                params
            )
            val result = getGeneratedLink(response)
            emit(result)
        }
            .asFlowResult()
            .flowOn(dispatchers.io)
    }

    private fun getGeneratedLink(response: ShareExGenerateAffiliateLinkWrapperResponseDto): String {
        val data = response.generateAffiliateLink.data.firstOrNull()

        return data?.let {
            val error = it.error
            val shortUrl = it.url.shortUrl
            val regularUrl = it.url.regularUrl

            when {
                error.isNotBlank() -> throw Throwable(error)
                shortUrl.isNotBlank() -> shortUrl
                regularUrl.isNotBlank() -> regularUrl
                else -> throw Throwable(ERROR_ALL_URLS_EMPTY)
            }
        } ?: throw Throwable(ERROR_DATA_EMPTY)
    }

    companion object {
        private const val ERROR_ALL_URLS_EMPTY = "Short & Regular URLs are empty"
        private const val ERROR_DATA_EMPTY = "Data is empty"
    }
}
