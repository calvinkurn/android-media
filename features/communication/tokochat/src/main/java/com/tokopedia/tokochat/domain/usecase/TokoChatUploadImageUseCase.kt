package com.tokopedia.tokochat.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.tokochat.data.repository.TokoChatImageRepository
import com.tokopedia.tokochat.domain.response.upload_image.TokoChatUploadImageResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class TokoChatUploadImageUseCase @Inject constructor(
    private val repository: TokoChatImageRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<TokoChatUploadImageUseCase.Param, TokoChatUploadImageResult>(dispatchers.io) {

    // Not used
    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: Param): TokoChatUploadImageResult {
        val file = File(params.filePath)
        val requestBodyFile = file.asRequestBody(IMAGE)
        val requestBodyChannelId = params.channelId.toRequestBody(TEXT)
        return repository.uploadImage(requestBodyFile, requestBodyChannelId)
    }

    data class Param(
        val filePath: String,
        val channelId: String
    )

    companion object {
        val IMAGE = "image/*".toMediaTypeOrNull()
        val TEXT = "text/plain".toMediaTypeOrNull()
    }
}
