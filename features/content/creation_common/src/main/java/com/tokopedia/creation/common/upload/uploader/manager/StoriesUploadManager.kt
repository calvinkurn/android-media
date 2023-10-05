package com.tokopedia.creation.common.upload.uploader.manager

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.const.CreationUploadConst
import com.tokopedia.creation.common.upload.domain.usecase.stories.StoriesAddMediaUseCase
import com.tokopedia.creation.common.upload.domain.usecase.stories.StoriesUpdateStoryUseCase
import com.tokopedia.creation.common.upload.model.ContentMediaType
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadResult
import com.tokopedia.creation.common.upload.model.dto.stories.StoriesAddMediaRequest
import com.tokopedia.creation.common.upload.model.dto.stories.StoriesUpdateStoryRequest
import com.tokopedia.creation.common.upload.model.stories.StoriesStatus
import com.tokopedia.creation.common.upload.uploader.notification.StoriesUploadNotificationManager
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.play_common.const.PlayUploadSourceIdConst
import com.tokopedia.play_common.types.PlayChannelStatusType
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
    private val notificationManager: StoriesUploadNotificationManager,
    private val uploaderUseCase: UploaderUseCase,
    private val updateStoryUseCase: StoriesUpdateStoryUseCase,
    private val addMediaUseCase: StoriesAddMediaUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val snapshotHelper: VideoSnapshotHelper,
) : CreationUploadManager {

    private var currentProgress = 0

    private lateinit var uploadData: CreationUploadData.Stories

    private var mListener: CreationUploadManagerListener? = null

    private val progressPerStep by lazyThreadSafetyNone {
        PROGRESS_MAX / STORIES_UPLOAD_STEP
    }

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
        listener: CreationUploadManagerListener
    ): Boolean {
        if (uploadData !is CreationUploadData.Stories) return false

        this.uploadData = uploadData
        this.mListener = listener

        return withContext(dispatchers.io) {
            try {
                broadcastInit(uploadData)

                /** TODO JOE: add logic here */
                updateStoryStatus(uploadData, StoriesStatus.Transcoding)

                val mediaUploadResult = uploadMedia(uploadData.firstMediaUri, uploadData.firstMediaType, uploadData.sourceId)
                val coverUrl = uploadCover(uploadData).fileUrl

                addMedia(uploadData, mediaUploadResult, coverUrl)

                updateStoryStatus(uploadData, StoriesStatus.Active)

                broadcastComplete()

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

                broadcastFail()

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

        if (status != StoriesStatus.TranscodingFailed) {
            updateProgress()
        }
    }

    private suspend fun uploadMedia(
        mediaUri: String,
        mediaType: ContentMediaType,
        sourceId: String,
    ): UploadResult.Success {
        val param = uploaderUseCase.createParams(
            sourceId = sourceId,
            filePath = File(mediaUri),
            withTranscode = true,
        )

        val result = uploaderUseCase(param)

        return when (result) {
            is UploadResult.Success -> {
                updateProgress()

                result
            }
            is UploadResult.Error -> {
                throw Exception(result.message)
            }
        }
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

        return uploadMedia(coverFilePath, ContentMediaType.Image, "")
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

        updateProgress()
    }

    private suspend fun updateProgress(progress: Int = progressPerStep) {
        currentProgress += progress
        broadcastProgress(currentProgress)
        notificationManager.onProgress(currentProgress)
    }

    private suspend fun broadcastInit(uploadData: CreationUploadData) {
        broadcastProgress(CreationUploadConst.PROGRESS_INIT)
        notificationManager.init(uploadData)
        mListener?.setupForegroundNotification(notificationManager.onStart())
    }

    private suspend fun broadcastComplete() {
        broadcastProgress(CreationUploadConst.PROGRESS_COMPLETED)
        notificationManager.onSuccess()
        delay(UPLOAD_FINISH_DELAY)
    }

    private suspend fun broadcastFail() {
        broadcastProgress(CreationUploadConst.PROGRESS_FAILED)
        notificationManager.onError()
        delay(UPLOAD_FINISH_DELAY)
    }

    private suspend fun broadcastProgress(progress: Int) {
        mListener?.setProgress(uploadData, progress)
    }

    private fun getSourceId(uploadType: UploadType): String {
        return when (uploadType) {
            UploadType.Image -> PlayUploadSourceIdConst.uploadImageSourceId
            UploadType.Video -> uploadData.sourceId
        }
    }

    enum class UploadType(
        val type: String,
        val withTranscode: Boolean
    ) {
        Image(UPLOAD_TYPE_IMAGE, false),
        Video(UPLOAD_TYPE_VIDEO, true)
    }

    companion object {
        private const val UPLOAD_TYPE_IMAGE = "UPLOAD_TYPE_IMAGE"
        private const val UPLOAD_TYPE_VIDEO = "UPLOAD_TYPE_VIDEO"

        private const val PROGRESS_MAX = 100
        private const val STORIES_UPLOAD_STEP = 5
        private const val UPLOAD_FINISH_DELAY = 1000L
    }
}
