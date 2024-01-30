package com.tokopedia.shareexperience.stub.domain.shortlink

import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkFallbackPriorityEnum
import com.tokopedia.shareexperience.domain.model.request.shortlink.ShareExShortLinkRequest
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetShortLinkUseCase
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShareExGetShortLinkUseCaseStub @Inject constructor(): ShareExGetShortLinkUseCase {
    override suspend fun getShortLink(params: ShareExShortLinkRequest): Flow<Pair<ShareExShortLinkFallbackPriorityEnum, ShareExResult<String>>> {
        return flow {

        }
    }
}
