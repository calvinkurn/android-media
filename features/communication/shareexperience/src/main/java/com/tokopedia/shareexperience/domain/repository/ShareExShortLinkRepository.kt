package com.tokopedia.shareexperience.domain.repository

import com.tokopedia.shareexperience.domain.ShareExResult
import kotlinx.coroutines.flow.Flow

interface ShareExShortLinkRepository {
    suspend fun generateShortLink(): Flow<ShareExResult<String>>
}
