package com.tokopedia.shareexperience.stub.domain

import com.tokopedia.shareexperience.domain.model.ShareExChannelEnum
import com.tokopedia.shareexperience.domain.model.imagegenerator.ShareExImageGeneratorModel
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorWrapperRequest
import com.tokopedia.shareexperience.domain.usecase.ShareExGetGeneratedImageUseCase
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShareExGetGeneratedImageUseCaseStub @Inject constructor(): ShareExGetGeneratedImageUseCase {
    override suspend fun getData(
        params: ShareExImageGeneratorWrapperRequest,
        channelEnum: ShareExChannelEnum
    ): Flow<ShareExResult<ShareExImageGeneratorModel>> {
        return flow {

        }
    }
}
