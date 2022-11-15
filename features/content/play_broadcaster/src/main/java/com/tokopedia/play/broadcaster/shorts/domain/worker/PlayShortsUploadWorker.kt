package com.tokopedia.play.broadcaster.shorts.domain.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsUploadUiModel
import kotlinx.coroutines.CoroutineScope
import java.io.File

/**
 * Created By : Jonathan Darwin on November 15, 2022
 */
class PlayShortsUploadWorker(
    context: Context,
    workerParam: WorkerParameters,
    private val uploaderUseCase: UploaderUseCase,
    private val scope: CoroutineScope,
) : Worker(context, workerParam) {

    override fun doWork(): Result {
        val uploadData = PlayShortsUploadUiModel.parse(inputData)

        scope.launchCatchError(block = {
            /** TODO 1: Update channel status to transcoding -> 6 */

            /** TODO 2: upload media */
            val mediaUrl = uploadMedia(uploadData.mediaUri)

            /** TODO 3: if no cover, upload cover */

            /** TODO 4: create media  */

            /** TODO 5: update channel status to 1 if success, 7 if transcoding failed  */
        }) {
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


    companion object {
        private const val SHORTS_UPLOAD_VIDEO_SOURCE_ID = "JQUJTn"
    }
}
