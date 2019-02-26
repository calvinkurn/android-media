package com.tokopedia.affiliate.feature.createpost.domain.usecase

import com.tokopedia.abstraction.common.data.model.session.UserSession
import com.tokopedia.affiliate.feature.createpost.data.pojo.uploadimage.UploadImageResponse
import com.tokopedia.affiliate.util.urlIsFile
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.SubmitPostMedium
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.util.*
import javax.inject.Inject

/**
 * @author by milhamj on 10/1/18.
 */
class UploadMultipleImageUseCase @Inject constructor(
        private val uploadImageUseCase: UploadImageUseCase<UploadImageResponse>,
        private val userSession: UserSession) : UseCase<List<SubmitPostMedium>>() {

    @Suppress("UNCHECKED_CAST")
    override fun createObservable(requestParams: RequestParams): Observable<List<SubmitPostMedium>> {
        return Observable.from(requestParams.getObject(PARAM_URL_LIST) as List<SubmitPostMedium>)
                .flatMap(uploadSingleImage())
                .toList()
    }

    private fun uploadSingleImage(): Func1<SubmitPostMedium, Observable<SubmitPostMedium>> {
        return Func1 { medium ->
            if (urlIsFile(medium.mediaURL)) {
                uploadImageUseCase.createObservable(createUploadParams(medium.mediaURL))
                        .map(mapToUrl(medium))
                        .subscribeOn(Schedulers.io())
            } else {
                Observable.just<SubmitPostMedium>(medium)
            }
        }
    }

    private fun mapToUrl(
            medium: SubmitPostMedium): Func1<ImageUploadDomainModel<UploadImageResponse>, SubmitPostMedium> {
        return Func1 { uploadDomainModel ->
            var imageUrl: String? = uploadDomainModel.dataResultImageUpload.data.picSrc
            if (imageUrl != null && imageUrl.contains(DEFAULT_RESOLUTION)) {
                imageUrl = imageUrl.replaceFirst(DEFAULT_RESOLUTION.toRegex(), RESOLUTION_500)
            }
            medium.mediaURL = imageUrl
            medium
        }
    }

    private fun createUploadParams(fileLocation: String?): RequestParams {
        val maps = HashMap<String, RequestBody>()
        val webService = RequestBody.create(
                MediaType.parse(TEXT_PLAIN),
                DEFAULT_WEB_SERVICE
        )
        val resolution = RequestBody.create(
                MediaType.parse(TEXT_PLAIN),
                RESOLUTION_500
        )
        val id = RequestBody.create(
                MediaType.parse(TEXT_PLAIN),
                userSession.userId + UUID.randomUUID() + System.currentTimeMillis()
        )
        maps[PARAM_WEB_SERVICE] = webService
        maps[PARAM_ID] = id
        maps[PARAM_RESOLUTION] = resolution
        return uploadImageUseCase.createRequestParam(
                fileLocation,
                DEFAULT_UPLOAD_PATH,
                DEFAULT_UPLOAD_TYPE,
                maps
        )
    }

    companion object {
        private const val PARAM_URL_LIST = "url_list"
        private const val PARAM_ID = "id"
        private const val PARAM_WEB_SERVICE = "web_service"
        private const val PARAM_RESOLUTION = "param_resolution"
        private const val DEFAULT_WEB_SERVICE = "1"
        private const val DEFAULT_UPLOAD_PATH = "/upload/attachment"
        private const val DEFAULT_UPLOAD_TYPE = "fileToUpload\"; filename=\"image.jpg"
        private const val DEFAULT_RESOLUTION = "100-square"
        private const val RESOLUTION_500 = "500"
        private const val TEXT_PLAIN = "text/plain"

        fun createRequestParams(mediumList: List<SubmitPostMedium>): RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PARAM_URL_LIST, mediumList)
            return requestParams
        }
    }
}
