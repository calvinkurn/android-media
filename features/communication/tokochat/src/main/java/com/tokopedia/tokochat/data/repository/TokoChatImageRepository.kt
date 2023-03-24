package com.tokopedia.tokochat.data.repository

import com.tokopedia.tokochat.data.repository.api.TokoChatDownloadImageApi
import com.tokopedia.tokochat.data.repository.api.TokoChatImageApi
import com.tokopedia.tokochat.data.repository.api.TokoChatUploadImageApi
import com.tokopedia.tokochat.domain.response.extension.TokoChatImageResult
import com.tokopedia.tokochat.domain.response.upload_image.TokoChatUploadImageResult
import okhttp3.RequestBody
import okhttp3.ResponseBody
import javax.inject.Inject

class TokoChatImageRepository @Inject constructor(
    private val tokoChatImageApi: TokoChatImageApi,
    private val tokoChatDownloadImageApi: TokoChatDownloadImageApi,
    private val tokoChatUploadImageApi: TokoChatUploadImageApi
) {
    suspend fun getImageUrl(imageId: String, channelId: String): TokoChatImageResult {
        return tokoChatImageApi.getImageUrl(imageId, channelId)
    }

    suspend fun getImage(url: String): ResponseBody {
        return tokoChatDownloadImageApi.getImage(url)
    }

    suspend fun uploadImage(file: RequestBody, channelId: RequestBody): TokoChatUploadImageResult {
        return tokoChatUploadImageApi.uploadImage(file = file, key = channelId)
    }
}
