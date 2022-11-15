package com.tokopedia.play.broadcaster.shorts.domain.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.shorts.domain.usecase.BroadcasterAddMediasUseCase
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsUploadUiModel
import com.tokopedia.play.broadcaster.shorts.util.PlayShortsSnapshotHelper
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.types.PlayChannelStatusType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

/**
 * Created By : Jonathan Darwin on November 15, 2022
 */
class PlayShortsUploadWorker(
    private val context: Context,
    private val workerParam: WorkerParameters,
    private val uploaderUseCase: UploaderUseCase,
    private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
    private val addMediaUseCase: BroadcasterAddMediasUseCase,
    private val scope: CoroutineScope,
    private val snapshotHelper: PlayShortsSnapshotHelper
) : Worker(context, workerParam) {

    override fun doWork(): Result {
        val uploadData = PlayShortsUploadUiModel.parse(inputData)

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
         */
        scope.launchCatchError(block = {
            updateChannelStatus(uploadData, PlayChannelStatusType.Transcoding)

            val mediaUrl = uploadMedia(UploadType.Video, uploadData.mediaUri)

            if (uploadData.coverUri.isEmpty()) {
                uploadFirstSnapshotAsCover(uploadData) {
                    addMediaAndUpdateChannel(uploadData, mediaUrl)
                }
            } else {
                addMediaAndUpdateChannel(uploadData, mediaUrl)
            }
        }) {
            updateChannelStatus(uploadData, PlayChannelStatusType.TranscodingFailed)

            snapshotHelper.deleteLocalFile()

            /** TODO: handle if error */
        }

        Log.d("<LOG>", uploadData.toString())
        return Result.success()
    }

    private suspend fun addMediaAndUpdateChannel(
        uploadData: PlayShortsUploadUiModel,
        mediaUrl: String
    ) {
        val activeMediaId = addMedia(uploadData, mediaUrl)
        updateChannelStatusWithMedia(uploadData, activeMediaId, PlayChannelStatusType.Active)
    }

    private suspend fun uploadMedia(
        uploadType: UploadType,
        mediaUri: String
    ): String {
        val param = uploaderUseCase.createParams(
            sourceId = uploadType.id,
            filePath = File(mediaUri),
            withTranscode = uploadType.withTranscode
        )

        uploaderUseCase.trackProgress {
            /** TODO: update progress
             * need to note that the you cant take the number and update the UI progress directly
             * because there's a lot of action that needs to be done here,
             * so don't forget NORMALIZE the progress percentage on the UI
             * */
        }

        val result = uploaderUseCase(param)

        return when (result) {
            is UploadResult.Success -> {
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

    private suspend fun uploadFirstSnapshotAsCover(uploadData: PlayShortsUploadUiModel, onSuccess: suspend () -> Unit) {
        snapshotHelper.snap(context, uploadData.mediaUri) {
            scope.launch {
                val uploadId = uploadMedia(UploadType.Image, it.absolutePath)

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

                onSuccess()
            }
        }
    }

    private suspend fun addMedia(
        uploadData: PlayShortsUploadUiModel,
        mediaUrl: String
    ): String {
        val result = addMediaUseCase.executeOnBackground(
            creationId = uploadData.shortsId,
            source = mediaUrl
        )

        if (result.wrapper.mediaIDs.isEmpty()) throw Exception("Active media ID is empty")

        return result.wrapper.mediaIDs.first()
    }

    private suspend fun updateChannelStatus(
        uploadData: PlayShortsUploadUiModel,
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
    }

    private suspend fun updateChannelStatusWithMedia(
        uploadData: PlayShortsUploadUiModel,
        activeMediaId: String,
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
    }

    enum class UploadType(
        val type: String,
        val id: String,
        val withTranscode: Boolean
    ) {
        Image(UPLOAD_TYPE_IMAGE, "jJtrdn", false),
        Video(UPLOAD_TYPE_VIDEO, "JQUJTn", true)
    }

    companion object {
        private const val UPLOAD_TYPE_IMAGE = "UPLOAD_TYPE_IMAGE"
        private const val UPLOAD_TYPE_VIDEO = "UPLOAD_TYPE_VIDEO"
    }
}
