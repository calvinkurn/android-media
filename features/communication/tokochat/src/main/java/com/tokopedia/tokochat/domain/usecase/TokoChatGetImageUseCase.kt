package com.tokopedia.tokochat.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.tokochat.data.repository.TokoChatImageRepository
import com.tokopedia.tokochat.domain.response.extension.TokoChatImageResult
import okhttp3.ResponseBody
import javax.inject.Inject

class TokoChatGetImageUseCase @Inject constructor(
    private val repository: TokoChatImageRepository,
    dispatchers: CoroutineDispatchers
): CoroutineUseCase<TokoChatGetImageUseCase.Param, TokoChatImageResult>(dispatchers.io) {

    // Not used
    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: Param): TokoChatImageResult {
        return repository.getImageUrl(imageId = params.imageId, channelId = params.channelId)
    }

    suspend fun getImage(url: String): ResponseBody {
        return repository.getImage(url)
    }

    data class Param (
        val imageId: String,
        val channelId: String
    )
}
