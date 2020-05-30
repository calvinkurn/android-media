package com.tokopedia.vouchercreation.create.domain.usecase

import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.create.domain.model.upload.ImageUploadResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import java.util.*
import javax.inject.Inject

class UploadVoucherUseCase @Inject constructor(
        private val uploadImageUseCase: UploadImageUseCase<ImageUploadResponse>,
        private val userSession: UserSessionInterface) : UseCase<ImageUploadResponse>() {

    companion object {
        private const val PARAM_ID = "id"
        private const val PARAM_RESOLUTION = "param_resolution"
        private const val DEFAULT_UPLOAD_PATH = "/upload/attachment"
        private const val DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg"
        private const val DEFAULT_RESOLUTION = "100-square"
        private const val RESOLUTION_500 = "500"
        private const val TEXT_PLAIN = "text/plain"
        private const val FILE_PREFIX = "file:"
        private const val IMAGE_PATH = "image_path"

        fun createRequestParams(imagePath: String):
                RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(IMAGE_PATH, imagePath)
            return requestParams
        }
    }

    override fun createObservable(requestParams: RequestParams): Observable<ImageUploadResponse> {
        return uploadImageUseCase.getExecuteObservable(
                createUploadParams(
                        requestParams.getString(IMAGE_PATH, "")))
                .flatMap { model ->
                    Observable.just<ImageUploadResponse>(model.dataResultImageUpload)
                }
    }

    private fun createUploadParams(fileLocation: String?): RequestParams {
        val maps = HashMap<String, RequestBody>()
        val resolution = RequestBody.create(
                MediaType.parse(TEXT_PLAIN),
                RESOLUTION_500
        )
        val id = RequestBody.create(
                MediaType.parse(TEXT_PLAIN),
                userSession.userId + UUID.randomUUID() + System.currentTimeMillis()
        )
        maps[PARAM_ID] = id
        maps[PARAM_RESOLUTION] = resolution
        return uploadImageUseCase.createRequestParam(
                fileLocation,
                DEFAULT_UPLOAD_PATH,
                DEFAULT_UPLOAD_TYPE,
                maps
        )
    }
}