package com.tokopedia.affiliate.feature.createpost.domain.usecase

import com.tokopedia.affiliate.feature.createpost.data.pojo.uploadimage.UploadImageResponse
import com.tokopedia.affiliate.feature.createpost.view.util.SubmitPostNotificationManager
import com.tokopedia.affiliate.feature.createpost.view.util.urlIsFile
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.SubmitPostMedium
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.imageuploader.domain.model.ImageUploadDomainModel
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.videouploader.domain.model.VideoUploadDomainModel
import com.tokopedia.videouploader.domain.pojo.DefaultUploadVideoResponse
import com.tokopedia.videouploader.domain.usecase.UploadVideoUseCase
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import rx.functions.Func1
import java.util.*
import javax.inject.Inject

/**
 * @author by milhamj on 10/1/18.
 */
class UploadMultipleImageUseCase @Inject constructor(
        private val uploadImageUseCase: UploadImageUseCase<UploadImageResponse>,
        private val uploadVideoUseCase: UploadVideoUseCase<DefaultUploadVideoResponse>,
        private val userSession: UserSessionInterface) : UseCase<List<SubmitPostMedium>>() {

    var notificationManager: SubmitPostNotificationManager? = null

    @Suppress("UNCHECKED_CAST")
    override fun createObservable(requestParams: RequestParams): Observable<List<SubmitPostMedium>> {
        val isUploadVideo = requestParams.getBoolean(IS_UPLOAD_VIDEO, false)
        return Observable.from(requestParams.getObject(PARAM_URL_LIST) as List<SubmitPostMedium>)
                .flatMap(if (isUploadVideo) uploadVideo() else uploadSingleImage())
                .toList()
    }

    private fun uploadVideo(): Func1<SubmitPostMedium, Observable<SubmitPostMedium>> {
        return Func1 { medium ->
            uploadVideoUseCase.createObservable(UploadVideoUseCase.createParam(medium.mediaURL))
                    .map(mapToUrlVideo(medium))
                    .map(updateNotification())
                    .subscribeOn(Schedulers.io())
        }
    }

    private fun uploadSingleImage(): Func1<SubmitPostMedium, Observable<SubmitPostMedium>> {
        return Func1 { medium ->
            if (urlIsFile(medium.mediaURL)) {
                uploadImageUseCase.createObservable(createUploadParams(medium.mediaURL))
                        .map(mapToUrl(medium))
                        .map(updateNotification())
            } else {
                Observable.just<SubmitPostMedium>(medium).map(updateNotification())
            }
        }
    }

    private fun mapToUrl(
            medium: SubmitPostMedium): Func1<ImageUploadDomainModel<UploadImageResponse>, SubmitPostMedium> {
        return Func1 { uploadDomainModel ->
            var imageUrl: String = uploadDomainModel.dataResultImageUpload.data.picSrc ?: ""
            if (imageUrl.contains(DEFAULT_RESOLUTION)) {
                imageUrl = imageUrl.replaceFirst(DEFAULT_RESOLUTION.toRegex(), RESOLUTION_500)
            }
            medium.mediaURL = imageUrl
            medium
        }
    }

    private fun mapToUrlVideo(
            medium: SubmitPostMedium): Func1<VideoUploadDomainModel<DefaultUploadVideoResponse>,
            SubmitPostMedium> {
        return Func1 { uploadDomainModel ->
            val videoId: String = uploadDomainModel?.dataResultVideoUpload?.videoId ?: ""
            val videoUrl: String = uploadDomainModel?.dataResultVideoUpload?.playbackList?.get(0)?.url ?: ""
            medium.id = videoId
            medium.mediaURL = videoUrl
            medium
        }
    }

    private fun updateNotification(): Func1<SubmitPostMedium, SubmitPostMedium> {
        return Func1 {
            notificationManager?.onAddProgress()
            it
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

        private const val IS_UPLOAD_VIDEO = "isUploadVideo"

        fun createRequestParams(mediumList: List<SubmitPostMedium>, isUploadVideo: Boolean):
                RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PARAM_URL_LIST, mediumList)
            requestParams.putBoolean(IS_UPLOAD_VIDEO, isUploadVideo)
            return requestParams
        }
    }
}
