package com.tokopedia.shop_nib.domain.usecase

import com.tokopedia.shop_nib.data.source.RemoteDataSource
import com.tokopedia.shop_nib.domain.entity.UploadFileResult
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun execute(fileUri: String, nibPublishedDate: String): UploadFileResult {
        return remoteDataSource.uploadFile(fileUri, nibPublishedDate)
    }

}
