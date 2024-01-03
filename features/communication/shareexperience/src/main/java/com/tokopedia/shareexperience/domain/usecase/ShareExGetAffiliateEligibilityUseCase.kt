package com.tokopedia.shareexperience.domain.usecase

import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.ShareExRequest
import com.tokopedia.shareexperience.domain.model.affiliate.ShareExAffiliateEligibilityModel
import kotlinx.coroutines.flow.Flow

interface ShareExGetAffiliateEligibilityUseCase {
    suspend fun getData(params: ShareExRequest): Flow<ShareExResult<ShareExAffiliateEligibilityModel>>
}
