package com.tokopedia.shareexperience.stub.domain

import android.net.Uri
import androidx.core.net.toUri
import com.tokopedia.shareexperience.domain.usecase.ShareExGetDownloadedImageUseCase
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class ShareExGetDownloadedImageUseCaseImplStub @Inject constructor(): ShareExGetDownloadedImageUseCase {
    override suspend fun downloadImageThumbnail(mediaUrl: String): Flow<ShareExResult<Uri>> {
        return flowOf(ShareExResult.Success("tokopedia://uri".toUri()))
    }
}
