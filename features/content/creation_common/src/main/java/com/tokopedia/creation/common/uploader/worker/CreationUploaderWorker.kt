package com.tokopedia.creation.common.uploader.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on September 15, 2023
 */
class CreationUploaderWorker(
    private val appContext: Context,
    workerParam: WorkerParameters
) : CoroutineWorker(appContext, workerParam) {

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    init {
        inject()
    }

    private fun inject() {

    }

    override suspend fun doWork(): Result {
        return withContext(dispatchers.io) {
            Result.success(workDataOf())
        }
    }
}
