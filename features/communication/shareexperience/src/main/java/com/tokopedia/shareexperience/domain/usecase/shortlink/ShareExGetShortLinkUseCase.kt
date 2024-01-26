package com.tokopedia.shareexperience.domain.usecase.shortlink

import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkFallbackPriorityEnum
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkRequest
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow

interface ShareExGetShortLinkUseCase {
    suspend fun getShortLink(
        params: ShareExShortLinkRequest
    ): Flow<Pair<ShareExShortLinkFallbackPriorityEnum, ShareExResult<String>>>
}
