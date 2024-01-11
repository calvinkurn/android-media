package com.tokopedia.shareexperience.domain.repository

import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import kotlinx.coroutines.flow.Flow

interface ShareExShortLinkRepository {
    suspend fun generateShortLink(params: ShareExBranchLinkPropertiesRequest): Flow<ShareExResult<String>>
}
