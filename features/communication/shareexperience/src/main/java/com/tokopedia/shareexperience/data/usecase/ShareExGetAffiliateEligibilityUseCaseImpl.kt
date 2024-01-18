package com.tokopedia.shareexperience.data.usecase

import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.coroutines.data.extensions.request
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.shareexperience.data.dto.affiliate.ShareExAffiliateLinkWrapperResponseDto
import com.tokopedia.shareexperience.data.repository.ShareExGetAffiliateEligibilityQuery
import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.asFlowResult
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateEligibilityModel
import com.tokopedia.shareexperience.domain.model.request.affiliate.ShareExAffiliateEligibilityRequest
import com.tokopedia.shareexperience.domain.model.request.affiliate.ShareExAffiliateLinkEligibilityRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetAffiliateEligibilityUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ShareExGetAffiliateEligibilityUseCaseImpl @Inject constructor(
    @ApplicationContext private val repository: GraphqlRepository,
    private val dispatchers: CoroutineDispatchers
) : ShareExGetAffiliateEligibilityUseCase {

    private val affiliateEligibilityQuery = ShareExGetAffiliateEligibilityQuery()

    override suspend fun getData(params: ShareExAffiliateEligibilityRequest): Flow<ShareExResult<ShareExAffiliateEligibilityModel>> {
        return flow {
            val dto = repository.request<ShareExAffiliateLinkEligibilityRequest, ShareExAffiliateLinkWrapperResponseDto>(
                affiliateEligibilityQuery,
                ShareExAffiliateLinkEligibilityRequest(params)
            )
            val result = ShareExAffiliateEligibilityModel(
                dto.affiliateLinkEligibilityResponseDto.eligibleCommission.isEligible
            )
            emit(result)
        }
            .asFlowResult()
            .flowOn(dispatchers.io)
    }
}
