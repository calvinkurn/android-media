package com.tokopedia.creation.common.upload.uploader.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.uploader.manager.CreationUploadManagerProvider
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

    @Inject
    lateinit var uploadManagerProvider: CreationUploadManagerProvider

    init {
        inject()
    }

    private fun inject() {

    }

    override suspend fun doWork(): Result {
        return withContext(dispatchers.io) {
            /**
             * 1. Read data from DB
             * 2. Get upload manager based on type
             * 3. Execute upload manager
             */
            Result.success(workDataOf())
        }
    }

    companion object {
        fun build(): OneTimeWorkRequest {
            return OneTimeWorkRequest.Builder(CreationUploaderWorker::class.java)
                .build()
        }
    }
}
