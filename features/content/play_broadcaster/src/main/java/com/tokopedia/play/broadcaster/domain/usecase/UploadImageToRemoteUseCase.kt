package com.tokopedia.play.broadcaster.domain.usecase

import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.play.broadcaster.data.model.PlayCoverUploadEntity
import com.tokopedia.play.broadcaster.util.error.DefaultNetworkThrowable
import com.tokopedia.usecase.coroutines.UseCase
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.MediaType
import okhttp3.RequestBody
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject

/**
 * Created by jegul on 14/07/20
 */
class UploadImageToRemoteUseCase @Inject constructor(
        private val uploadImageUseCase: UploadImageUseCase<PlayCoverUploadEntity>,
        userSession: UserSessionInterface
) : UseCase<PlayCoverUploadEntity>() {

    private val params = mapOf<String, RequestBody>(
            PARAM_WEB_SERVICE to RequestBody.create(MediaType.parse(TEXT_PLAIN), DEFAULT_WEB_SERVICE),
            PARAM_RESOLUTION to RequestBody.create(MediaType.parse(TEXT_PLAIN), RESOLUTION_700),
            PARAM_ID to RequestBody.create(MediaType.parse(TEXT_PLAIN),
                    "${userSession.userId}${UUID.randomUUID()}${System.currentTimeMillis()}")
    )

    private var imagePath: String = ""

    override suspend fun executeOnBackground(): PlayCoverUploadEntity {
        try {
            return uploadImageUseCase
                    .createObservable(uploadImageUseCase.createRequestParam(
                            imagePath,
                            DEFAULT_UPLOAD_PATH,
                            DEFAULT_UPLOAD_TYPE,
                            params))
                    .toBlocking()
                    .first()
                    .dataResultImageUpload
        } catch (e: Throwable) {
            throw if (e.hasCauseType(listOf(UnknownHostException::class.java, SocketTimeoutException::class.java))) DefaultNetworkThrowable()
            else e
        }
    }

    fun setImagePath(imagePath: String) {
        this.imagePath = imagePath
    }

    private fun Throwable.hasCauseType(errorClassList: List<Class<out Throwable>>): Boolean {
        var cause: Throwable? = this
        while (cause?.cause != null) {
            cause = cause.cause
            if (cause!!::class.java in errorClassList) return true
        }
        return false
    }

    companion object {
        private const val PARAM_ID = "id"
        private const val PARAM_WEB_SERVICE = "web_service"
        private const val PARAM_RESOLUTION = "param_resolution"
        private const val DEFAULT_WEB_SERVICE = "1"
        private const val DEFAULT_UPLOAD_PATH = "/upload/attachment"
        private const val DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg"
        private const val TEXT_PLAIN = "text/plain"
        private const val RESOLUTION_700 = "700"
    }
}