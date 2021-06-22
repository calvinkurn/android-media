package com.tokopedia.rechargeocr.viewmodel

import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.rechargeocr.data.RechargeUploadImageResponse
import com.tokopedia.usecase.RequestParams
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.RequestBody
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class RechargeUploadImageUseCase @Inject constructor(private val uploadImageUseCase: UploadImageUseCase<RechargeUploadImageResponse>,
                                                     private val userSession: UserSessionInterface) {

    suspend fun execute(fileLocation: String?, dispatcher: CoroutineDispatcher = Dispatchers.IO): RechargeUploadImageResponse {
        return withContext(dispatcher) {
            try {
                uploadImageUseCase.createObservable(createUploadParams(fileLocation))
                        .toBlocking().first().dataResultImageUpload
            } catch (throwable: Throwable) {
                if (throwable is RuntimeException && throwable.cause != null) {
                    throw throwable.cause ?: throwable
                } else throw throwable
            }
        }
    }

    private fun createUploadParams(fileLocation: String?): RequestParams {
        val maps = HashMap<String, RequestBody>()
        val webService = RequestBody.create(
                MediaType.parse(TEXT_PLAIN), DEFAULT_WEB_SERVICE
        )
        val resolution = RequestBody.create(
                MediaType.parse(TEXT_PLAIN), RESOLUTION_500
        )
        val id = RequestBody.create(
                MediaType.parse(TEXT_PLAIN),
                userSession.userId + UUID.randomUUID() + System.currentTimeMillis()
        )
        maps[PARAM_WEB_SERVICE] = webService
        maps[PARAM_ID] = id
        maps[PARAM_RESOLUTION] = resolution
        return uploadImageUseCase.createRequestParam(fileLocation, DEFAULT_UPLOAD_PATH, DEFAULT_UPLOAD_TYPE, maps
        )
    }

    companion object {
        private const val PARAM_ID = "id"
        private const val PARAM_WEB_SERVICE = "web_service"
        private const val PARAM_RESOLUTION = "param_resolution"
        private const val DEFAULT_WEB_SERVICE = "1"
        private const val DEFAULT_UPLOAD_PATH = "/upload/attachment"
        private const val DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg"
        private const val RESOLUTION_500 = "500"
        private const val TEXT_PLAIN = "text/plain"
    }
}