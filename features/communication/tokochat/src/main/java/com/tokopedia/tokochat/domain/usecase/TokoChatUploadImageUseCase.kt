package com.tokopedia.tokochat.domain.usecase

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.tokochat.data.repository.TokoChatImageRepository
import com.tokopedia.tokochat.domain.response.upload_image.TokoChatUploadImageResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class TokoChatUploadImageUseCase @Inject constructor(
    private val repository: TokoChatImageRepository,
    dispatchers: CoroutineDispatchers
) : CoroutineUseCase<TokoChatUploadImageUseCase.Param, TokoChatUploadImageResponse>(dispatchers.io) {

    // Not used
    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: Param): TokoChatUploadImageResponse {
        val file = File(params.filePath)
        val requestBodyFile = file.asRequestBody(IMAGE)
        val multiPartFile = MultipartBody.Part.createFormData(FILE_PART, file.name, requestBodyFile)
        val requestBodyChannelId = params.channelId.toRequestBody(TEXT)
        return repository.uploadImage(multiPartFile, requestBodyChannelId)
    }

    data class Param(
        val filePath: String,
        val channelId: String
    )

    companion object {
        private const val FILE_PART = "file"
        val IMAGE = "image/jpeg".toMediaTypeOrNull()
        val TEXT = "text/plain".toMediaTypeOrNull()

        const val ERROR_PAYLOAD_NOT_EXPECTED = "Error payload not expected"
    }
}
