package com.tokopedia.shareexperience.stub.domain.shortlink

import com.tokopedia.shareexperience.domain.model.request.shortlink.branch.ShareExBranchLinkPropertiesRequest
import com.tokopedia.shareexperience.domain.usecase.shortlink.ShareExGetBranchLinkUseCase
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShareExGetBranchLinkUseCaseStub @Inject constructor(): ShareExGetBranchLinkUseCase {

    override suspend fun getLink(params: ShareExBranchLinkPropertiesRequest): Flow<ShareExResult<String>> {
        return flow {

        }
    }
}
