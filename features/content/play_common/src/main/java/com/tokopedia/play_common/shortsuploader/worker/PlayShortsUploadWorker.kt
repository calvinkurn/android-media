package com.tokopedia.play_common.shortsuploader.worker

import android.content.Context
import androidx.work.*
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.domain.usecase.broadcaster.BroadcasterAddMediasUseCase
import com.tokopedia.play_common.domain.usecase.broadcaster.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play_common.shortsuploader.const.PlayShortsUploadConst
import com.tokopedia.play_common.shortsuploader.di.DaggerPlayShortsWorkerComponent
import com.tokopedia.play_common.shortsuploader.model.PlayShortsUploadModel
import com.tokopedia.play_common.shortsuploader.notification.PlayShortsUploadNotificationManager
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.VideoSnapshotHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 15, 2022
 */
class PlayShortsUploadWorker(
    private val appContext: Context,
    workerParam: WorkerParameters
) : CoroutineWorker(appContext, workerParam) {

    @Inject
    lateinit var uploaderUseCase: UploaderUseCase

    @Inject
    lateinit var updateChannelUseCase: PlayBroadcastUpdateChannelUseCase

    @Inject
    lateinit var addMediaUseCase: BroadcasterAddMediasUseCase

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    @Inject
    lateinit var snapshotHelper: VideoSnapshotHelper

    @Inject
    lateinit var notificationManager: PlayShortsUploadNotificationManager

    private var currentProgress = 0

    private val uploadData = PlayShortsUploadModel.parse(inputData)

    private val step = if (uploadData.coverUri.isEmpty()) STEP_WITHOUT_COVER else STEP_WITH_COVER

    private val progressPerStep = PROGRESS_MAX / step

    init {
        inject()
    }

    private fun inject() {
        DaggerPlayShortsWorkerComponent.builder()
            .baseAppComponent((appContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    /**
     * Upload Flow
     * 1. Update channel status to Transcoding (value = 6)
     * 2. Upload media to uploadpedia
     * 3. If user doesnt upload cover:
     *    3.a. Take first frame snapshot of selected media
     *    3.a. Upload snapshot to uploadpedia
     *    3.b. Update channel status (updating cover)
     * 4. Add media to broadcaster backend
     * 5. Update channel status to Active (value = 1)
     *
     * If error happens anywhere within the flow:
     * 1. Update channel status to TranscodingFailed (value = 7)
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
    override suspend fun doWork(): Result {
        return withContext(dispatchers.io) {
            try {
                broadcastInit()
                updateChannelStatus(uploadData, PlayChannelStatusType.Transcoding)

                val mediaUrl = uploadMedia(UploadType.Video, uploadData.mediaUri, withUpdateProgress = true)

                if (uploadData.coverUri.isEmpty()) {
                    uploadFirstSnapshotAsCover(uploadData)
                }

                val activeMediaId = addMedia(uploadData, mediaUrl)
                updateChannelStatusWithMedia(activeMediaId, uploadData, PlayChannelStatusType.Active)

                broadcastComplete()

                Result.success(workDataOf(PlayShortsUploadConst.SHORTS_ID to uploadData.shortsId))
            } catch (e: Exception) {
                /**
                 * updateChannelStatus to TranscodingFailed may error
                 * if no network connection
                 */
                try {
                    snapshotHelper.deleteLocalFile()
                    updateChannelStatus(uploadData, PlayChannelStatusType.TranscodingFailed)
                } catch (e: Exception) {
                }

                broadcastFail()

                Result.failure(inputData)
            }
        }
    }

    private suspend fun uploadMedia(
        uploadType: UploadType,
        mediaUri: String,
        withUpdateProgress: Boolean
    ): String {
        val param = uploaderUseCase.createParams(
            sourceId = getSourceId(uploadType),
            filePath = File(mediaUri),
            withTranscode = uploadType.withTranscode
        )

        val result = uploaderUseCase(param)

        return when (result) {
            is UploadResult.Success -> {
                if (withUpdateProgress) updateProgress()

                if (uploadType.type == UPLOAD_TYPE_IMAGE) {
                    result.uploadId
                } else {
                    result.videoUrl
                }
            }
            is UploadResult.Error -> {
                throw Exception(result.message)
            }
        }
    }

    private suspend fun uploadFirstSnapshotAsCover(
        uploadData: PlayShortsUploadModel
    ) {
        val bitmap = snapshotHelper.snapVideo(appContext, uploadData.mediaUri)
            ?: throw Exception("Gagal upload cover")

        val uploadId = uploadMedia(UploadType.Image, bitmap.absolutePath, withUpdateProgress = false)

        updateChannelUseCase.apply {
            setQueryParams(
                PlayBroadcastUpdateChannelUseCase.createUpdateFullCoverRequest(
                    channelId = uploadData.shortsId,
                    authorId = uploadData.authorId,
                    coverUrl = uploadId
                )
            )
        }.executeOnBackground()

        snapshotHelper.deleteLocalFile()
    }

    private suspend fun addMedia(
        uploadData: PlayShortsUploadModel,
        mediaUrl: String
    ): String {
        val request = addMediaUseCase.getShortsRequest(
            creationId = uploadData.shortsId,
            source = mediaUrl
        )

        val result = addMediaUseCase.executeOnBackground(request)

        if (result.wrapper.mediaIDs.isEmpty()) throw Exception("Active media ID is empty")

        updateProgress()

        return result.wrapper.mediaIDs.first()
    }

    private suspend fun updateChannelStatus(
        uploadData: PlayShortsUploadModel,
        status: PlayChannelStatusType
    ) {
        updateChannelUseCase.apply {
            setQueryParams(
                UpdateChannelUseCase.createUpdateStatusRequest(
                    channelId = uploadData.shortsId,
                    authorId = uploadData.authorId,
                    status = status
                )
            )
        }.executeOnBackground()

        if (status != PlayChannelStatusType.TranscodingFailed) {
            updateProgress()
        }
    }

    private suspend fun updateChannelStatusWithMedia(
        activeMediaId: String,
        uploadData: PlayShortsUploadModel,
        status: PlayChannelStatusType
    ) {
        updateChannelUseCase.apply {
            setQueryParams(
                UpdateChannelUseCase.createUpdateStatusWithActiveMediaRequest(
                    channelId = uploadData.shortsId,
                    authorId = uploadData.authorId,
                    status = status,
                    activeMediaId = activeMediaId
                )
            )
        }.executeOnBackground()

        updateProgress()
    }

    private suspend fun updateProgress(progress: Int = progressPerStep) {
        currentProgress += progress
        broadcastProgress(currentProgress)
        notificationManager.onProgress(currentProgress)
    }

    private suspend fun broadcastInit() {
        broadcastProgress(PlayShortsUploadConst.PROGRESS_INIT)
        notificationManager.init(uploadData)
        setForegroundAsync(notificationManager.onStart())
    }

    private suspend fun broadcastComplete() {
        broadcastProgress(PlayShortsUploadConst.PROGRESS_COMPLETED)
        notificationManager.onSuccess()
        delay(UPLOAD_FINISH_DELAY)
    }

    private suspend fun broadcastFail() {
        broadcastProgress(PlayShortsUploadConst.PROGRESS_FAILED)
        notificationManager.onError()
        delay(UPLOAD_FINISH_DELAY)
    }

    private suspend fun broadcastProgress(progress: Int) {
        val data = workDataOf(
            PlayShortsUploadConst.PROGRESS to progress,
            PlayShortsUploadConst.UPLOAD_DATA to uploadData.toString()
        )

        setProgress(
            workDataOf(
                PlayShortsUploadConst.PROGRESS to progress,
                PlayShortsUploadConst.UPLOAD_DATA to uploadData.toString()
            )
        )
    }

    private fun getSourceId(uploadType: UploadType): String {
        return when (uploadType) {
            UploadType.Image -> UPLOAD_IMAGE_SOURCE_ID
            UploadType.Video -> uploadData.shortsVideoSourceId
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

        private const val UPLOAD_IMAGE_SOURCE_ID = "jJtrdn"

        private const val PROGRESS_MAX = 100
        private const val STEP_WITHOUT_COVER = 5
        private const val STEP_WITH_COVER = 4
        private const val UPLOAD_FINISH_DELAY = 1000L

        fun build(uploadModel: PlayShortsUploadModel): OneTimeWorkRequest {
            return OneTimeWorkRequest.Builder(PlayShortsUploadWorker::class.java)
                .setInputData(uploadModel.format())
                .build()
        }
    }
}
