package com.tokopedia.chatbot.domain.usecase

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.tokopedia.chat_common.data.ImageUploadViewModel
import com.tokopedia.chatbot.data.uploadsecure.UploadSecureResponse
import okhttp3.RequestBody
import javax.inject.Inject

const val IMAGE_UPLOAD_URL = "https://u12.tokopedia.net"
private const val IMAGE_UPLOAD_PATH = "/upload/attachment"
private const val ATTACHMENT_TYPE = "fileToUpload\"; filename=\"image.jpg"
const val IMAGE_QUALITY = 70
private const val PARAM_WEB_SERVICE = "web_service"
private const val PARAM_ID = "id"


private const val SERVER_ID = "server_id"

class UploadSecureImageUploadUseCase @Inject constructor(private val repository: ContactUsRepository) {

    suspend fun getSecureImageUploadUrl(
        body: Map<String, RequestBody>,
        imageUploadViewModel: ImageUploadViewModel,
        context: Context
    ): UploadSecureResponse {
        val secureImageParameter = repository.postMultiRestData<UploadSecureResponse>(
            "https://chat-staging.tokopedia.com/tc/v1/upload_secure",
            object : TypeToken<UploadSecureResponse>() {}.type,
            body = body,
            context = context
        )
        return secureImageParameter
    }

    suspend fun downloadImage(url: String): UploadSecureResponse {
        val secureImage = repository.getRestData<UploadSecureResponse>(
            url,
            object : TypeToken<UploadSecureResponse>() {}.type
        )
        return secureImage
    }
}