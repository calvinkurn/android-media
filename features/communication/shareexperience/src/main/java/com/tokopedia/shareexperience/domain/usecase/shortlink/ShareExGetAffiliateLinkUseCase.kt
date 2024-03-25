package com.tokopedia.shareexperience.domain.usecase.shortlink

import com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate.ShareExAffiliateLinkPropertiesWrapperRequest
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow

interface ShareExGetAffiliateLinkUseCase {
    suspend fun getLink(
        params: ShareExAffiliateLinkPropertiesWrapperRequest
    ): Flow<ShareExResult<String>>
}
