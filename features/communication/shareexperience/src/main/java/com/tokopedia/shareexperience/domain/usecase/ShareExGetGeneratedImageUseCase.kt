package com.tokopedia.shareexperience.domain.usecase

import com.tokopedia.shareexperience.domain.model.imagegenerator.ShareExImageGeneratorModel
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorWrapperRequest
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow

interface ShareExGetGeneratedImageUseCase {
    suspend fun getData(
        params: ShareExImageGeneratorWrapperRequest
    ): Flow<ShareExResult<ShareExImageGeneratorModel>>
}
