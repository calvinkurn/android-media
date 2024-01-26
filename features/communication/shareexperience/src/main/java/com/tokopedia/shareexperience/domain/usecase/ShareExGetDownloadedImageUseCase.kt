package com.tokopedia.shareexperience.domain.usecase

import android.net.Uri
import com.tokopedia.shareexperience.domain.util.ShareExResult
import kotlinx.coroutines.flow.Flow

interface ShareExGetDownloadedImageUseCase {
    suspend fun downloadImage(imageUrl: String): Flow<ShareExResult<Uri>>
}
