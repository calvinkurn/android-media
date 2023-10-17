package com.tokopedia.creation.common.upload.uploader.manager

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.domain.usecase.stories.StoriesAddMediaUseCase
import com.tokopedia.creation.common.upload.domain.usecase.stories.StoriesUpdateStoryUseCase
import com.tokopedia.creation.common.upload.model.ContentMediaType
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadStatus
import com.tokopedia.creation.common.upload.model.dto.stories.StoriesAddMediaRequest
import com.tokopedia.creation.common.upload.model.dto.stories.StoriesUpdateStoryRequest
import com.tokopedia.creation.common.upload.model.stories.StoriesStatus
import com.tokopedia.creation.common.upload.uploader.notification.StoriesUploadNotificationManager
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.play_common.const.PlayUploadSourceIdConst
import com.tokopedia.play_common.util.VideoSnapshotHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class StoriesUploadManager @Inject constructor(
    @ApplicationContext private val appContext: Context,
    notificationManager: StoriesUploadNotificationManager,
    private val uploaderUseCase: UploaderUseCase,
    private val updateStoryUseCase: StoriesUpdateStoryUseCase,
    private val addMediaUseCase: StoriesAddMediaUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val snapshotHelper: VideoSnapshotHelper,
) : CreationUploadManager(notificationManager) {

    private lateinit var uploadData: CreationUploadData.Stories

    private var isUploadStoryMedia = false

    /**
     * Upload Flow
     * 1. Update story status to Transcoding (value = 5)
     * 2. Upload media to uploadpedia (GQL : Uploader)
     * 3. Take first frame snapshot of selected media & upload (GQL : Uploader)
     * 4. Add mediaId & coverUrl to backend (GQL : AddMedia)
     * 5. Update story status to Active (value = 1)
     *
     * If error happens anywhere within the flow:
     * 1. Update story status to TranscodingFailed (value = 6)
     *
     * Note:
     * Result.success & Result.failure() is not used here because
     * WorkManager has caching mechanism for the last emitted state.
     * Handling Result.success & Result.failure will show the last state
     * in the upcoming observe.
     * So, to determine whether the result is success or not,
     * we are using [progress] variable:
     * 100 : success
     * -1 : failed
     * else : loading
     */

    override suspend fun execute(
        uploadData: CreationUploadData,
        notificationId: Int,
    ): Boolean {
        if (uploadData !is CreationUploadData.Stories) return false

        this.uploadData = uploadData

        withContext(dispatchers.main) {
            uploaderUseCase.trackProgress { progress, progressType ->
                if (progressType is ProgressType.Upload && isUploadStoryMedia) {
                    if (progress == MAX_UPLOAD_PROGRESS) {
                        broadcastProgress(uploadData, CreationUploadStatus.OtherProcess)
                    } else {
                        updateProgress(uploadData, progress)
                    }
                }
            }
        }

        return withContext(dispatchers.io) {
            try {
                broadcastInit(uploadData, notificationId)

                updateStoryStatus(uploadData, StoriesStatus.Transcoding)

                val mediaUploadResult = uploadStoryMedia(uploadData.firstMediaUri, uploadData.firstMediaType)
                val coverUrl = uploadCover(uploadData).fileUrl

                addMedia(uploadData, mediaUploadResult, coverUrl)

                updateStoryStatus(uploadData, StoriesStatus.Active)

                broadcastComplete(uploadData)

                true
            } catch (e: Exception) {
                /**
                 * update story status to TranscodingFailed may error
                 * if no network connection
                 */
                try {
                    snapshotHelper.deleteLocalFile()
                    updateStoryStatus(uploadData, StoriesStatus.TranscodingFailed)
                } catch (e: Exception) {
                }

                broadcastFail(uploadData)

                false
            }
        }
    }

    private suspend fun updateStoryStatus(
        uploadData: CreationUploadData.Stories,
        status: StoriesStatus,
        activeMediaId: String = ""
    ) {
        updateStoryUseCase(
            StoriesUpdateStoryRequest(
                storyId = uploadData.creationId,
                activeMediaId = activeMediaId,
                status = status
            )
        )
    }

    private suspend fun uploadStoryMedia(
        mediaUri: String,
        mediaType: ContentMediaType,
    ): UploadResult.Success {
        isUploadStoryMedia = true
        val result = uploadMedia(mediaUri, mediaType)
        isUploadStoryMedia = false

        return result
    }

    private suspend fun uploadCover(
        uploadData: CreationUploadData.Stories,
    ): UploadResult.Success {
        val coverFilePath = if (uploadData.firstMediaType == ContentMediaType.Video) {
            val bitmap = snapshotHelper.snapVideo(appContext, uploadData.firstMediaUri)
                ?: throw Exception("Gagal upload cover")

            bitmap.absolutePath
        } else {
            uploadData.firstMediaUri
        }

        return uploadMedia(coverFilePath, ContentMediaType.Image)
    }

    private suspend fun uploadMedia(
        mediaUri: String,
        mediaType: ContentMediaType,
    ): UploadResult.Success {
        val param = uploaderUseCase.createParams(
            sourceId = getSourceId(mediaType),
            filePath = File(mediaUri),
            withTranscode = true,
        )

        return when (val result = uploaderUseCase(param)) {
            is UploadResult.Success -> {
                result
            }
            is UploadResult.Error -> {
                throw Exception(result.message)
            }
        }
    }

    private suspend fun addMedia(
        uploadData: CreationUploadData.Stories,
        mediaUploadResult: UploadResult.Success,
        coverUrl: String,
    ) {
        addMediaUseCase(
            StoriesAddMediaRequest(
                storyId = uploadData.creationId,
                type = uploadData.firstMediaType.code,
                mediaUrl = if (uploadData.firstMediaType == ContentMediaType.Image) {
                    mediaUploadResult.fileUrl
                } else {
                    mediaUploadResult.videoUrl
                },
                coverUrl = coverUrl,
                uploadId = mediaUploadResult.uploadId,
            )
        )
    }

    private fun getSourceId(contentMediaType: ContentMediaType): String {
        return when (contentMediaType) {
            ContentMediaType.Image -> uploadData.imageSourceId
            ContentMediaType.Video -> uploadData.videoSourceId
            else -> ""
        }
    }
}
