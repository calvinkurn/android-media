package com.tokopedia.vouchercreation.create.domain.usecase

import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.create.domain.model.upload.ImageUploadResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import rx.functions.Func1
import javax.inject.Inject

class UploadVoucherUseCase @Inject constructor(
        private val uploadImageUseCase: UploadImageUseCase<ImageUploadResponse.ImageUploadData>,
        private val userSession: UserSessionInterface) : UseCase<MutableList<String?>>() {

    companion object {
        private const val PARAM_ID = "id"
        private const val PARAM_TOKEN = "token"
        private const val PARAM_RESOLUTION = "resolution"
        private const val DEFAULT_UPLOAD_PATH = "/upload/attachment"
        private const val DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg"
        private const val DEFAULT_RESOLUTION = "/cache/100-square"
        private const val RESOLUTION_500 = "500"
        private const val TEXT_PLAIN = "text/plain"
        private const val IMAGES_PATH = "images_path"

        fun createRequestParams(vararg urls: String): RequestParams =
            RequestParams.create().apply {
                putObject(IMAGES_PATH, urls.toList())
            }
    }

    override fun createObservable(requestParams: RequestParams): Observable<MutableList<String?>> {
        val imagesField = requestParams.parameters[IMAGES_PATH]
        val images = (imagesField as? List<Any?>)?.filterIsInstance<String>().orEmpty()
        val uploadParamSource = images.map { createUploadParams(it) }
        return with(uploadParamSource) {
            Observable.from(this)
                    .flatMap { uploadImageUseCase.createObservable(it) }
                    .map(mapToHighResolutionUrl())
                    .filter { it != null }
                    .toList()
        }
    }

    private fun mapToHighResolutionUrl(): Func1<ImageUploadDomainModel<ImageUploadResponse.ImageUploadData>, String> =
            Func1 { model ->
                val lowResUrl = model.dataResultImageUpload.picSrc.toBlankOrString()
                if (lowResUrl.contains(DEFAULT_RESOLUTION)) {
                    lowResUrl.replaceFirst(DEFAULT_RESOLUTION.toRegex(), "")
                } else {
                    lowResUrl
                }
            }

    private fun createUploadParams(fileLocation: String): RequestParams {
        val id = RequestBody.create(MediaType.parse(TEXT_PLAIN), userSession.userId)
        val token = RequestBody.create(MediaType.parse(TEXT_PLAIN), userSession.accessToken)
        val resolution = RequestBody.create(MediaType.parse(TEXT_PLAIN), RESOLUTION_500)
        val maps = mapOf(PARAM_ID to id, PARAM_TOKEN to token, PARAM_RESOLUTION to resolution)
        return uploadImageUseCase.createRequestParam(
                fileLocation,
                DEFAULT_UPLOAD_PATH,
                DEFAULT_UPLOAD_TYPE,
                maps
        )
    }
}