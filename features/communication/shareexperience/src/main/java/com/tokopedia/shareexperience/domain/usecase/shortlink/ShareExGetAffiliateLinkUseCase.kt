package com.tokopedia.shareexperience.domain.usecase.shortlink

import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate.ShareExAffiliateLinkPropertiesRequest
import kotlinx.coroutines.flow.Flow

interface ShareExGetAffiliateLinkUseCase {
    suspend fun getLink(params: ShareExAffiliateLinkPropertiesRequest): Flow<ShareExResult<String>>
}
