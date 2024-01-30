package com.tokopedia.shareexperience.stub.domain

import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateEligibilityModel
import com.tokopedia.shareexperience.domain.model.request.affiliate.ShareExAffiliateEligibilityRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetAffiliateEligibilityUseCase
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShareExGetAffiliateEligibilityUseCaseStub @Inject constructor(): ShareExGetAffiliateEligibilityUseCase {
    override suspend fun getData(params: ShareExAffiliateEligibilityRequest): Flow<ShareExResult<ShareExAffiliateEligibilityModel>> {
        return flow {

        }
    }
}
