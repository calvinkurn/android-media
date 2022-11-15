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
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.types.PlayChannelStatusType
import kotlinx.coroutines.CoroutineScope
import java.io.File

/**
 * Created By : Jonathan Darwin on November 15, 2022
 */
class PlayShortsUploadWorker(
    context: Context,
    workerParam: WorkerParameters,
    private val uploaderUseCase: UploaderUseCase,
    private val updateChannelUseCase: PlayBroadcastUpdateChannelUseCase,
    private val addMediaUseCase: BroadcasterAddMediasUseCase,
    private val scope: CoroutineScope,
) : Worker(context, workerParam) {

    override fun doWork(): Result {
        val uploadData = PlayShortsUploadUiModel.parse(inputData)

        scope.launchCatchError(block = {
            updateChannelStatus(uploadData, PlayChannelStatusType.Transcoding)

            val mediaUrl = uploadMedia(uploadData.mediaUri)

            /** TODO 3: if no cover, upload cover */
            if(uploadData.coverUri.isEmpty()) uploadFirstSnapshotAsCover(uploadData)

            val activeMediaId = addMedia(uploadData, mediaUrl)

            updateChannelStatusWithMedia(uploadData, activeMediaId, PlayChannelStatusType.Active)
        }) {
            updateChannelStatus(uploadData, PlayChannelStatusType.TranscodingFailed)
            /** TODO: handle if error */
        }


        Log.d("<LOG>", uploadData.toString())
        return Result.success()
    }

    private suspend fun uploadMedia(mediaUri: String): String {
        val param = uploaderUseCase.createParams(
            sourceId = SHORTS_UPLOAD_VIDEO_SOURCE_ID,
            filePath = File(mediaUri),
            withTranscode = true,
        )

        uploaderUseCase.trackProgress {
            /** TODO: handle this */
        }

        val result = uploaderUseCase(param)

        return when(result) {
            is UploadResult.Success -> {
                result.videoUrl
            }
            is UploadResult.Error -> {
                throw Exception(result.message)
            }
        }
    }

    private suspend fun uploadFirstSnapshotAsCover(uploadData: PlayShortsUploadUiModel) {

    }

    private suspend fun addMedia(
        uploadData: PlayShortsUploadUiModel,
        mediaUrl: String,
    ): String {
        val result = addMediaUseCase.executeOnBackground(
            creationId = uploadData.shortsId,
            source = mediaUrl,
        )

        if(result.wrapper.mediaIDs.isEmpty()) throw Exception("Active media ID is empty")

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
                    activeMediaId = activeMediaId,
                )
            )
        }.executeOnBackground()
    }

    companion object {
        private const val SHORTS_UPLOAD_VIDEO_SOURCE_ID = "JQUJTn"
    }
}
