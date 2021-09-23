package com.tokopedia.createpost.domain.usecase

import android.content.Context
import android.net.Uri
import com.tokopedia.affiliatecommon.data.pojo.submitpost.request.SubmitPostMedium
import com.tokopedia.createpost.data.pojo.uploadimage.UploadImageResponse
import com.tokopedia.createpost.view.util.FileUtil
import com.tokopedia.createpost.view.util.PostUpdateProgressManager
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
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class UploadMultipleImageUsecaseNew @Inject constructor(
    private val uploadImageUseCase: UploadImageUseCase<UploadImageResponse>,
    private val uploadVideoUseCase: UploadVideoUseCase<DefaultUploadVideoResponse>,
    private val userSession: UserSessionInterface) : UseCase<List<SubmitPostMedium>>() {

    var postUpdateProgressManager: PostUpdateProgressManager? = null
    var tempFilePath=""
    var firstTimeUpoad = true

    @Suppress("UNCHECKED_CAST")
    override fun createObservable(requestParams: RequestParams): Observable<List<SubmitPostMedium>> {
        return Observable.from(requestParams.getObject(PARAM_URL_LIST) as List<SubmitPostMedium>)
            .flatMap { if (it.type == SubmitPostMedium.TYPE_VIDEO) uploadVideo(it) else uploadSingleImage(it) }
            .toList()
    }

    private fun uploadVideo(medium: SubmitPostMedium): Observable<SubmitPostMedium> {
            return uploadVideoUseCase.createObservable(UploadVideoUseCase.createParam(setTempFilePath(medium))).
            map(mapToUrlVideo(medium))
    }

    private fun uploadSingleImage(medium: SubmitPostMedium): Observable<SubmitPostMedium> {
            return uploadImageUseCase.createObservable(createUploadParams(setTempFilePath(medium))).
            map(mapToUrl(medium))
    }

    private fun handleUri(medium: SubmitPostMedium):String{
       val filePath =  mContext?.let { FileUtil.createFilePathFromUri(it,Uri.parse(medium.mediaURL)) }
        if (filePath != null) {
            medium.mediaURL= filePath
        }
        return medium.mediaURL
    }

    private fun setTempFilePath(medium: SubmitPostMedium): String {
        if (medium.mediaURL.contains("content://")) {
            tempFilePath = handleUri(medium)
        } else {
            tempFilePath = medium.mediaURL
        }
        return tempFilePath
    }

    private fun mapToUrl(
        medium: SubmitPostMedium): Func1<ImageUploadDomainModel<UploadImageResponse>, SubmitPostMedium> {
        return Func1 { uploadDomainModel ->
            var imageUrl: String = uploadDomainModel.dataResultImageUpload.data.picSrc ?: ""
            if (imageUrl.contains(DEFAULT_RESOLUTION)) {
                imageUrl = imageUrl.replaceFirst(DEFAULT_RESOLUTION.toRegex(), RESOLUTION_500)
            }
            if (firstTimeUpoad) {
                postUpdateProgressManager?.setFirstIcon(imageUrl)
                firstTimeUpoad = false
            }
            postUpdateProgressManager?.onAddProgress()
            deleteCacheFile()
            medium.mediaURL = imageUrl
            medium
        }
    }

    private fun mapToUrlVideo(
        medium: SubmitPostMedium): Func1<VideoUploadDomainModel<DefaultUploadVideoResponse>,
            SubmitPostMedium> {
        return Func1 { uploadDomainModel ->
            val videoId: String = uploadDomainModel?.dataResultVideoUpload?.videoId ?: ""
            val videoUrl: String = uploadDomainModel?.dataResultVideoUpload?.playbackList?.get(0)?.url
                ?: ""
            if (firstTimeUpoad) {
                postUpdateProgressManager?.setFirstIcon(videoUrl)
                firstTimeUpoad = false
            }
            postUpdateProgressManager?.onAddProgress()
            deleteCacheFile()
            medium.videoID = videoId
            medium.mediaURL = videoUrl
            medium
        }
    }

    private fun deleteCacheFile() {
        if (File(tempFilePath).exists()) {
            File(tempFilePath).delete()
        }
    }

    private fun updateNotification(): Func1<SubmitPostMedium, SubmitPostMedium> {
        return Func1 {
            postUpdateProgressManager?.onAddProgress()
            it
        }
    }

    private fun createUploadParams(fileToUpload:String): RequestParams {
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
            fileToUpload,
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

        var mContext: Context? = null

        fun createRequestParams(mediumList: List<SubmitPostMedium>):
                RequestParams {
            val requestParams = RequestParams.create()
            requestParams.putObject(PARAM_URL_LIST, mediumList)
            return requestParams
        }
    }
}
