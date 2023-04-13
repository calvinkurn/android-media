package com.tokopedia.shop_nib.domain.usecase

import com.tokopedia.shop_nib.data.response.UploadFileResponse
import com.tokopedia.shop_nib.data.source.RemoteDataSource
import javax.inject.Inject

class UploadFileUseCase @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun execute(fileUri: String): UploadFileResponse {
        return remoteDataSource.uploadFile(fileUri)
    }

}
