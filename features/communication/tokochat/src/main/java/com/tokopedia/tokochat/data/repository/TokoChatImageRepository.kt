package com.tokopedia.tokochat.data.repository

import com.tokopedia.tokochat.data.repository.api.TokoChatDownloadImageApi
import com.tokopedia.tokochat.data.repository.api.TokoChatImageApi
import com.tokopedia.tokochat.domain.response.extension.TokoChatImageResult
import okhttp3.ResponseBody
import javax.inject.Inject

class TokoChatImageRepository @Inject constructor(
    private val tokoChatImageApi: TokoChatImageApi,
    private val tokoChatDownloadImageApi: TokoChatDownloadImageApi
) {
    suspend fun getImageUrl(imageId: String, channelId: String): TokoChatImageResult {
        return tokoChatImageApi.getImageUrl(imageId, channelId)
    }

    suspend fun getImage(url: String): ResponseBody {
        return tokoChatDownloadImageApi.getImage(url)
    }
}
