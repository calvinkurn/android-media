package com.tokopedia.creation.common.upload.uploader.manager

import android.content.Context
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.model.ContentMediaType
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.CreationUploadStatus
import com.tokopedia.creation.common.upload.uploader.notification.ShortsUploadNotificationManager
import com.tokopedia.creation.common.upload.util.plus
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.play_common.const.PlayUploadSourceIdConst
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.domain.usecase.broadcaster.BroadcasterAddMediasUseCase
import com.tokopedia.play_common.domain.usecase.broadcaster.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play_common.types.PlayChannelStatusType
import com.tokopedia.play_common.util.VideoSnapshotHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class ShortsUploadManager @AssistedInject constructor(
    @ApplicationContext private val appContext: Context,
    @Assisted private val uploadData: CreationUploadData.Shorts,
    private val notificationManager: ShortsUploadNotificationManager,
    private val uploaderUseCase: UploaderUseCase,
    private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
    private val addMediaUseCase: BroadcasterAddMediasUseCase,
    private val dispatchers: CoroutineDispatchers,
    private val snapshotHelper: VideoSnapshotHelper,
) : CreationUploadManager(notificationManager) {

    @AssistedFactory
    interface Factory {
        fun create(uploadData: CreationUploadData.Shorts): ShortsUploadManager
    }

    private var isUploadShortsMedia = false

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
     *
     *
     */
    override suspend fun execute(
        notificationId: Int,
    ): CreationUploadExecutionResult {

        setupInitialData()

        withContext(dispatchers.main) {
            uploaderUseCase.trackProgress { progress, type ->
                if (type is ProgressType.Upload && isUploadShortsMedia) {
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
                updateChannelStatus(uploadData, PlayChannelStatusType.Transcoding)

                val mediaUrl = uploadShortsMedia(uploadData.firstMediaUri)

                if (uploadData.coverUri.isEmpty()) {
                    uploadFirstSnapshotAsCover(uploadData)
                }

                val activeMediaId = addMedia(uploadData, mediaUrl)
                updateChannelStatusWithMedia(activeMediaId, uploadData, PlayChannelStatusType.Active)

                broadcastComplete(uploadData)

                CreationUploadExecutionResult.Success
            } catch (throwable: Throwable) {
                /**
                 * updateChannelStatus to TranscodingFailed may error
                 * if no network connection
                 */

                var loggedThrowable = throwable

                try {
                    snapshotHelper.deleteLocalFile()
                    updateChannelStatus(uploadData, PlayChannelStatusType.TranscodingFailed)
                } catch (fallbackThrowable: Throwable) {
                    loggedThrowable += fallbackThrowable
                }

                broadcastFail(uploadData)

                CreationUploadExecutionResult.Error(
                    uploadData,
                    loggedThrowable
                )
            }
        }
    }

    private suspend fun uploadShortsMedia(
        mediaUri: String,
    ): String {
        isUploadShortsMedia = true
        val mediaUrl = uploadMedia(mediaUri, ContentMediaType.Video)
        isUploadShortsMedia = false

        return mediaUrl
    }

    private fun setupInitialData() {
        this.isUploadShortsMedia = false
    }

    private suspend fun uploadMedia(
        mediaUri: String,
        mediaType: ContentMediaType,
    ): String {
        val param = uploaderUseCase.createParams(
            sourceId = getSourceId(mediaType),
            filePath = File(mediaUri),
            withTranscode = mediaType == ContentMediaType.Video
        )

        val result = uploaderUseCase(param)

        return when (result) {
            is UploadResult.Success -> {
                if (mediaType == ContentMediaType.Image) {
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
        uploadData: CreationUploadData.Shorts
    ) {
        val bitmap = snapshotHelper.snapVideo(appContext, uploadData.firstMediaUri)
            ?: throw Exception("Gagal upload cover")

        val uploadId = uploadMedia(bitmap.absolutePath, ContentMediaType.Image)

        updateChannelUseCase.apply {
            setQueryParams(
                PlayBroadcastUpdateChannelUseCase.createUpdateFullCoverRequest(
                    channelId = uploadData.creationId,
                    authorId = uploadData.authorId,
                    coverUrl = uploadId
                )
            )
        }.executeOnBackground()

        snapshotHelper.deleteLocalFile()
    }

    private suspend fun addMedia(
        uploadData: CreationUploadData.Shorts,
        mediaUrl: String
    ): String {
        val request = addMediaUseCase.getShortsRequest(
            creationId = uploadData.creationId,
            source = mediaUrl
        )

        val result = addMediaUseCase.executeOnBackground(request)

        if (result.wrapper.mediaIDs.isEmpty()) throw Exception("Active media ID is empty")

        return result.wrapper.mediaIDs.first()
    }

    private suspend fun updateChannelStatus(
        uploadData: CreationUploadData.Shorts,
        status: PlayChannelStatusType
    ) {
        updateChannelUseCase.apply {
            setQueryParams(
                UpdateChannelUseCase.createUpdateStatusRequest(
                    channelId = uploadData.creationId,
                    authorId = uploadData.authorId,
                    status = status
                )
            )
        }.executeOnBackground()
    }

    private suspend fun updateChannelStatusWithMedia(
        activeMediaId: String,
        uploadData: CreationUploadData.Shorts,
        status: PlayChannelStatusType
    ) {
        updateChannelUseCase.apply {
            setQueryParams(
                UpdateChannelUseCase.createUpdateStatusWithActiveMediaRequest(
                    channelId = uploadData.creationId,
                    authorId = uploadData.authorId,
                    status = status,
                    activeMediaId = activeMediaId,
                    isInterspersed = uploadData.isInterspersed,
                )
            )
        }.executeOnBackground()
    }

    private fun getSourceId(mediaType: ContentMediaType): String {
        return when (mediaType) {
            ContentMediaType.Image -> PlayUploadSourceIdConst.uploadImageSourceId
            ContentMediaType.Video -> uploadData.sourceId
            else -> ""
        }
    }
}
