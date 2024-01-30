package com.tokopedia.shareexperience.stub.domain

import android.net.Uri
import com.tokopedia.shareexperience.domain.usecase.ShareExGetDownloadedImageUseCase
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ShareExGetDownloadedImageUseCaseStub @Inject constructor(): ShareExGetDownloadedImageUseCase {
    override suspend fun downloadImage(imageUrl: String): Flow<ShareExResult<Uri>> {
        return flow {

        }
    }
}
