package com.tokopedia.shareexperience.domain.usecase.shortlink

import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow

interface ShareExGetBranchLinkUseCase {
    suspend fun getLink(
        params: ShareExBranchLinkPropertiesRequest
    ): Flow<ShareExResult<String>>
}
