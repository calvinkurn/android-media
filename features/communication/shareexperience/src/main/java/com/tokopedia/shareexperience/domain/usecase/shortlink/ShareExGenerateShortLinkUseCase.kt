package com.tokopedia.shareexperience.domain.usecase.shortlink

import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkRequest
import kotlinx.coroutines.flow.Flow

interface ShareExGenerateShortLinkUseCase {
    suspend fun getShortLink(params: ShareExShortLinkRequest): Flow<ShareExResult<String>>
}
