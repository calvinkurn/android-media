package com.tokopedia.creation.common.upload.uploader.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.const.CreationUploadConst
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.model.CreationUploadQueue
import com.tokopedia.creation.common.upload.uploader.manager.CreationUploadManagerProvider
import com.tokopedia.creation.common.upload.di.worker.DaggerCreationUploadWorkerComponent
import com.tokopedia.creation.common.upload.uploader.manager.CreationUploadManagerListener
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

    @Inject
    lateinit var queueRepository: CreationUploadQueueRepository

    init {
        inject()
    }

    private fun inject() {
        DaggerCreationUploadWorkerComponent
            .builder()
            .baseAppComponent((appContext as BaseMainApplication).baseAppComponent)
            .build()
            .inject(this)
    }

    override suspend fun doWork(): Result {
        return withContext(dispatchers.io) {
            /**
             * 1. Read data from DB
             * 2. Get upload manager based on type
             * 3. Execute upload manager
             */

            while(true) {
                val data = queueRepository.getTopQueue() ?: break

                if (data == CreationUploadQueue.Empty) continue

                try {
                    val uploadManager = uploadManagerProvider.get(data.uploadType)
                    uploadManager.execute(
                        data,
                        object : CreationUploadManagerListener {
                            override suspend fun setupForegroundNotification(info: ForegroundInfo) {
                                setForegroundAsync(info)
                            }

                            override suspend fun setProgress(uploadData: CreationUploadQueue, progress: Int) {
                                setProgress(
                                    workDataOf(
                                        CreationUploadConst.PROGRESS to progress,
                                        CreationUploadConst.UPLOAD_DATA to uploadData.toString()
                                    )
                                )
                            }
                        }
                    )

                    queueRepository.delete(data)
                } catch (throwable: Throwable) {
                    break
                }
            }

            Result.success()
        }
    }

    companion object {
        fun build(): OneTimeWorkRequest {
            return OneTimeWorkRequest.Builder(CreationUploaderWorker::class.java)
                .build()
        }
    }
}
