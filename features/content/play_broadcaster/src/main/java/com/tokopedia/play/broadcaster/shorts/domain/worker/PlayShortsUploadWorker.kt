package com.tokopedia.play.broadcaster.shorts.domain.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.shorts.di.DaggerPlayShortsComponent
import com.tokopedia.play.broadcaster.shorts.di.PlayShortsModule
import com.tokopedia.play.broadcaster.shorts.domain.usecase.BroadcasterAddMediasUseCase
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsUploadUiModel
import com.tokopedia.play.broadcaster.shorts.util.PlayShortsSnapshotHelper
import com.tokopedia.play_common.domain.UpdateChannelUseCase
import com.tokopedia.play_common.types.PlayChannelStatusType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on November 15, 2022
 */
class PlayShortsUploadWorker(
    private val appContext: Context,
    private val workerParam: WorkerParameters,
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
    lateinit var snapshotHelper: PlayShortsSnapshotHelper

    init {
        inject()
    }

    private fun inject() {
        DaggerPlayShortsComponent.builder()
            .baseAppComponent((appContext as BaseMainApplication).baseAppComponent)
            .playShortsModule(PlayShortsModule(appContext))
            .build()
            .inject(this)
    }

    override suspend fun doWork(): Result {
        return withContext(dispatchers.io) {

            val uploadData = PlayShortsUploadUiModel.parse(inputData)
            Log.d("<LOG>", "Start Uploading...")
            Log.d("<LOG>", uploadData.toString())
            Log.d("<LOG>", updateChannelUseCase.toString())
            Log.d("<LOG>", addMediaUseCase.toString())
            Log.d("<LOG>", dispatchers.toString())
            Log.d("<LOG>", snapshotHelper.toString())

            delay(1000)
            setProgress(workDataOf("progress" to 20))
            delay(1000)
            setProgress(workDataOf("progress" to 40))
            delay(1000)
            setProgress(workDataOf("progress" to 60))
            delay(1000)
            setProgress(workDataOf("progress" to 80))
            delay(1000)
            setProgress(workDataOf("progress" to 100))

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
//            try {
//                updateChannelStatus(uploadData, PlayChannelStatusType.Transcoding)
//
//                val mediaUrl = uploadMedia(UploadType.Video, uploadData.mediaUri)
//
//                if (uploadData.coverUri.isEmpty()) {
//                    uploadFirstSnapshotAsCover(this, uploadData, {
//                        addMediaAndUpdateChannel(uploadData, mediaUrl)
//                    }) {
//                        throw it
//                    }
//                } else {
//                    addMediaAndUpdateChannel(uploadData, mediaUrl)
//                }
//            }
//            catch (e: Exception) {
//                updateChannelStatus(uploadData, PlayChannelStatusType.TranscodingFailed)
//
//                snapshotHelper.deleteLocalFile()
//
//                /** TODO: handle if error */
//
//                Result.failure()
//            }

            Result.success()
        }
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

    private suspend fun uploadFirstSnapshotAsCover(
        scope: CoroutineScope,
        uploadData: PlayShortsUploadUiModel,
        onSuccess: suspend () -> Unit,
        onError: suspend (Throwable) -> Unit
    ) {
        snapshotHelper.snap(appContext, uploadData.mediaUri) {
            scope.launchCatchError(block = {
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
            }) {
                onError(it)
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
