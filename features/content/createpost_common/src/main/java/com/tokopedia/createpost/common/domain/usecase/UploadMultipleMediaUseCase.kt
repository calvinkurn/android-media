package com.tokopedia.createpost.common.domain.usecase

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.tokopedia.createpost.common.domain.entity.request.SubmitPostMedium
import com.tokopedia.createpost.common.di.ActivityContext
import com.tokopedia.createpost.common.di.qualifier.SubmitPostCoroutineScope
import com.tokopedia.createpost.common.domain.entity.UploadMediaDataModel
import com.tokopedia.createpost.common.view.util.FileUtil
import com.tokopedia.createpost.common.view.util.PostUpdateProgressManager
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.videouploader.domain.model.VideoUploadDomainModel
import com.tokopedia.videouploader.domain.pojo.DefaultUploadVideoResponse
import com.tokopedia.videouploader.domain.usecase.UploadVideoUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import java.io.File
import javax.inject.Inject
import kotlin.Exception

/**
 * Revamped By : Jonathan Darwin on October 13, 2022
 */
class UploadMultipleMediaUseCase @Inject constructor(
    @ActivityContext private val context: Context,
    @SubmitPostCoroutineScope private val scope: CoroutineScope,
    private val uploaderUseCase: UploaderUseCase,
    private val uploadVideoUseCase: UploadVideoUseCase<DefaultUploadVideoResponse>, /** Will be removed after video uploader migration is done soon */
    graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<List<SubmitPostMedium>>(graphqlRepository) {

    var postUpdateProgressManager: PostUpdateProgressManager? = null
    var tempFilePath = ""

    private val _images = MutableStateFlow<UploadMediaDataModel.Media>(UploadMediaDataModel.Media.Unknown)
    private val _videos = MutableStateFlow<UploadMediaDataModel.Media>(UploadMediaDataModel.Media.Unknown)

    val state: Flow<UploadMediaDataModel> = combine(
        _images,
        _videos
    ) { images, videos ->
        UploadMediaDataModel(
            images = images,
            videos = videos,
        )
    }

    suspend fun execute(mediaList: List<SubmitPostMedium>) {
        scope.launch {
            val images = mediaList.filter { !isVideo(it) }
            val videos = mediaList.filter { isVideo(it) }

            launchCatchError(block = { uploadImages(images) }) { throwable ->
                _images.update { UploadMediaDataModel.Media.Fail(throwable) }
            }

            launchCatchError(block = { uploadVideos(videos) }) { throwable ->
                _videos.update { UploadMediaDataModel.Media.Fail(throwable) }
            }
        }
    }

    private suspend fun uploadImages(mediumList: List<SubmitPostMedium>) {
        if(mediumList.isEmpty()) {
            _images.update { UploadMediaDataModel.Media.Success(emptyList()) }
            return
        }

        val newMedium = mediumList.map {
            uploadMedia(it).also {
                updateProgress()
            }
        }

        _images.update {
            UploadMediaDataModel.Media.Success(newMedium)
        }
    }

    private fun uploadVideos(mediumList: List<SubmitPostMedium>) {
        if(mediumList.isEmpty()) {
            _videos.update { UploadMediaDataModel.Media.Success(emptyList()) }
            return
        }

        Observable.from(mediumList)
            .flatMap { medium ->
                uploadVideoUseCase.createObservable(UploadVideoUseCase.createParam(setTempFilePath(medium)))
                    .map(mapToUrlVideo(medium))
                    .subscribeOn(Schedulers.io())
            }
            .toList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ newMediumList ->
                _videos.update {
                    UploadMediaDataModel.Media.Success(newMediumList)
                }
            }, { throwable ->
                _videos.update { UploadMediaDataModel.Media.Fail(throwable) }
            })
    }

    private fun mapToUrlVideo(
        medium: SubmitPostMedium
    ): Func1<VideoUploadDomainModel<DefaultUploadVideoResponse>, SubmitPostMedium> {
        return Func1 { uploadDomainModel ->
            val videoId: String = uploadDomainModel?.dataResultVideoUpload?.videoId ?: ""
            val videoUrl: String = uploadDomainModel?.dataResultVideoUpload?.playbackList?.get(0)?.url
                ?: ""
            postUpdateProgressManager?.addProgress()
            postUpdateProgressManager?.onAddProgress()
            deleteCacheFile()
            medium.videoID = videoId
            medium.mediaURL = videoUrl

            medium
        }
    }

    /**
     * The code below will be used when we have migrated
     * both image & video uploader to uploadpedia
     *
     * TODO IMPORTANT: still need to handle:
     * 1. async upload
     * 2. error handling if one of the media is failed upload
     */
    suspend fun executeOnBackground(mediaList: List<SubmitPostMedium>): List<SubmitPostMedium> {
        return mediaList.map {
            uploadMedia(it).also {
                updateProgress()
            }
        }
    }

    private suspend fun uploadMedia(medium: SubmitPostMedium): SubmitPostMedium {
        val param = uploaderUseCase.createParams(
            sourceId = getUploadSourceId(medium),
            filePath = File(setTempFilePath(medium)),
            withTranscode = getUploadWithTranscode(medium),
        )

        val result = uploaderUseCase(param)

        when(result) {
            is UploadResult.Success -> {
                if(isVideo(medium)) {
                    medium.videoID = result.uploadId
                    medium.mediaURL = result.videoUrl
                }
                else {
                    medium.mediaUploadID = result.uploadId
                    medium.mediaURL = ""
                    medium.type = SubmitPostMedium.TYPE_MEDIA_IMAGE_UPLOAD_ID
                }

                deleteCacheFile()
            }
            is UploadResult.Error -> {
                throw Exception(result.message)
            }
        }

        return medium
    }

    private fun isVideo(medium: SubmitPostMedium): Boolean {
        return medium.type == SubmitPostMedium.TYPE_VIDEO
    }

    private fun getUploadSourceId(medium: SubmitPostMedium): String {
        return if(isVideo(medium)) UPLOAD_VIDEO_SOURCE_ID
        else UPLOAD_IMAGE_SOURCE_ID
    }

    private fun getUploadWithTranscode(medium: SubmitPostMedium): Boolean {
        return !isVideo(medium)
    }

    private fun updateProgress() {
        postUpdateProgressManager?.addProgress()
        postUpdateProgressManager?.onAddProgress()
    }

    private fun handleUri(medium: SubmitPostMedium): String{
       val filePath =  context?.let { FileUtil.createFilePathFromUri(it,Uri.parse(medium.mediaURL)) }
        if (filePath != null) {
            medium.mediaURL= filePath
        }
        return medium.mediaURL
    }

    private fun setTempFilePath(medium: SubmitPostMedium): String {
        if (medium.mediaURL.contains("${ContentResolver.SCHEME_CONTENT}://")) {
            tempFilePath = handleUri(medium)
        } else {
            tempFilePath = medium.mediaURL
        }
        return tempFilePath
    }

    private fun deleteCacheFile() {
        if (File(tempFilePath).exists()) {
            File(tempFilePath).delete()
        }
    }

    companion object {
        private const val UPLOAD_IMAGE_SOURCE_ID = "ZiLyCt"
        private const val UPLOAD_VIDEO_SOURCE_ID = "jbPRuq"
    }
}
