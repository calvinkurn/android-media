package com.tokopedia.shareexperience.stub.domain.shortlink

import com.tokopedia.shareexperience.domain.model.request.shortlink.affiliate.ShareExAffiliateLinkPropertiesWrapperRequest
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetAffiliateLinkUseCase
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShareExGetAffiliateLinkUseCaseStub @Inject constructor(): ShareExGetAffiliateLinkUseCase {
    override suspend fun getLink(params: ShareExAffiliateLinkPropertiesWrapperRequest): Flow<ShareExResult<String>> {
        return flow {

        }
    }
}
