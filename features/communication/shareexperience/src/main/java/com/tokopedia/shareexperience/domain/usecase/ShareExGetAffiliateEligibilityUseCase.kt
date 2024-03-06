package com.tokopedia.shareexperience.domain.usecase

import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateEligibilityModel
import com.tokopedia.shareexperience.domain.model.request.affiliate.ShareExAffiliateEligibilityRequest
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow

interface ShareExGetAffiliateEligibilityUseCase {
    suspend fun getData(
        params: ShareExAffiliateEligibilityRequest
    ): Flow<ShareExResult<ShareExAffiliateEligibilityModel>>
}
