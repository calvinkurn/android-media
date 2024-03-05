package com.tokopedia.shareexperience.domain.usecase

import android.net.Uri
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow

interface ShareExGetDownloadedImageUseCase {
    suspend fun downloadImageThumbnail(mediaUrl: String): Flow<ShareExResult<Uri>>
}
