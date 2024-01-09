package com.tokopedia.shareexperience.domain.usecase

import com.tokopedia.shareexperience.domain.ShareExResult
import com.tokopedia.shareexperience.domain.model.imagegenerator.ShareExImageGeneratorModel
import com.tokopedia.shareexperience.domain.model.request.imagegenerator.ShareExImageGeneratorRequest
import kotlinx.coroutines.flow.Flow

interface ShareExGetGeneratedImageUseCase {
    suspend fun getData(
        params: ShareExImageGeneratorRequest
    ): Flow<ShareExResult<ShareExImageGeneratorModel>>
}
