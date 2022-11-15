package com.tokopedia.play.broadcaster.shorts.domain.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.tokopedia.play.broadcaster.shorts.ui.model.PlayShortsUploadUiModel

/**
 * Created By : Jonathan Darwin on November 15, 2022
 */
class PlayShortsUploadWorker(
    context: Context,
    workerParam: WorkerParameters,
) : Worker(context, workerParam) {

    override fun doWork(): Result {
        val uploadData = PlayShortsUploadUiModel.parse(inputData)
        Log.d("<LOG>", uploadData.toString())
        return Result.success()
    }
}
