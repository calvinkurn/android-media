package com.tokopedia.createpost.common.domain.usecase

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.createpost.common.di.qualifier.SubmitPostCoroutineScope
import com.tokopedia.createpost.common.domain.entity.UploadMediaDataModel
import com.tokopedia.createpost.common.domain.entity.request.SubmitPostRequest
import com.tokopedia.createpost.common.view.util.FileUtil
import com.tokopedia.createpost.common.view.util.PostUpdateProgressManager
import com.tokopedia.graphql.coroutines.domain.interactor.GraphqlUseCase
import com.tokopedia.graphql.coroutines.domain.repository.GraphqlRepository
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import java.io.File
import javax.inject.Inject

/**
 * Revamped By : Jonathan Darwin on October 13, 2022
 */
class UploadMultipleMediaUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    @SubmitPostCoroutineScope private val scope: CoroutineScope,
    private val uploaderUseCase: UploaderUseCase,
    @ApplicationContext graphqlRepository: GraphqlRepository,
) : GraphqlUseCase<List<SubmitPostRequest>>(graphqlRepository) {

    var postUpdateProgressManager: PostUpdateProgressManager? = null

    private val mapTempFilePath = mutableSetOf<String>()

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

    suspend fun execute(
        mediaList: List<SubmitPostRequest>,
        onSuccessUploadPerMedia: suspend () -> Unit,
    ): List<SubmitPostRequest> {
        val deferredList = mediaList.map { media ->
            scope.async {
                uploadMedia(media).also {
                    onSuccessUploadPerMedia()
                }
            }
        }

        return deferredList.awaitAll()
    }

    /**
     * The code below will be used when we have migrated
     * both image & video uploader to uploadpedia
     *
     * TODO IMPORTANT: still need to handle:
     * 1. async upload
     * 2. error handling if one of the media is failed upload
     */
    suspend fun executeOnBackground(mediaList: List<SubmitPostRequest>): List<SubmitPostRequest> {
        return mediaList.map {
            uploadMedia(it).also {
                updateProgress()
            }
        }
    }

    private suspend fun uploadMedia(media: SubmitPostRequest): SubmitPostRequest {
        val param = uploaderUseCase.createParams(
            sourceId = getUploadSourceId(media),
            filePath = File(setTempFilePath(media)),
            withTranscode = getUploadWithTranscode(media),
        )

        val result = uploaderUseCase(param)

        when(result) {
            is UploadResult.Success -> {
                if(isVideo(media)) {
                    media.videoID = result.uploadId
                    media.mediaURL = result.videoUrl
                }
                else {
                    media.mediaUploadID = result.uploadId
                    media.mediaURL = ""
                    media.type = SubmitPostRequest.TYPE_MEDIA_IMAGE_UPLOAD_ID
                }
            }
            is UploadResult.Error -> {
                throw Exception(result.message)
            }
        }

        return media
    }

    private fun isVideo(media: SubmitPostRequest): Boolean {
        return media.type == SubmitPostRequest.TYPE_VIDEO
    }

    private fun getUploadSourceId(media: SubmitPostRequest): String {
        return if(isVideo(media)) UPLOAD_VIDEO_SOURCE_ID
        else UPLOAD_IMAGE_SOURCE_ID
    }

    private fun getUploadWithTranscode(media: SubmitPostRequest): Boolean {
        return !isVideo(media)
    }

    private fun updateProgress() {
        postUpdateProgressManager?.addProgress()
        postUpdateProgressManager?.onAddProgress()
    }

    private fun handleUri(media: SubmitPostRequest): String{
        val filePath =  context.let { FileUtil.createFilePathFromUri(it,Uri.parse(media.mediaURL)) }
        if (filePath.isNotEmpty()) {
            media.mediaURL = filePath
        }
        return media.mediaURL
    }

    private fun setTempFilePath(media: SubmitPostRequest): String {
        val tempFilePath = if (media.mediaURL.contains("${ContentResolver.SCHEME_CONTENT}://")) {
            handleUri(media)
        } else {
            media.mediaURL
        }

        mapTempFilePath.add(tempFilePath)

        return tempFilePath
    }

    companion object {
        private const val UPLOAD_IMAGE_SOURCE_ID = "ZiLyCt"
        private const val UPLOAD_VIDEO_SOURCE_ID = "jbPRuq"
    }
}
