package com.tokopedia.shareexperience.domain.usecase

import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorWrapperRequest
import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import kotlinx.coroutines.flow.Flow

interface ShareExGetShortLinkUseCase {
    suspend fun getShortLink(
        imageGeneratorParams: ShareExImageGeneratorWrapperRequest,
        linkPropertiesParams: ShareExBranchLinkPropertiesRequest
    ): Flow<ShareExResult<String>>
}
