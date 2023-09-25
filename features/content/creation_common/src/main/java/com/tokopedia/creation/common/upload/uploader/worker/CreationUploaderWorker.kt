package com.tokopedia.creation.common.upload.uploader.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.creation.common.upload.const.CreationUploadConst
import com.tokopedia.creation.common.upload.domain.repository.CreationUploadQueueRepository
import com.tokopedia.creation.common.upload.uploader.manager.CreationUploadManagerProvider
import com.tokopedia.creation.common.upload.di.worker.DaggerCreationUploadWorkerComponent
import com.tokopedia.creation.common.upload.model.CreationUploadData
import com.tokopedia.creation.common.upload.model.exception.NoUploadManagerException
import com.tokopedia.creation.common.upload.model.exception.UnknownUploadTypeException
import com.tokopedia.creation.common.upload.uploader.manager.CreationUploadManagerListener
import kotlinx.coroutines.delay
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

    @Inject
    lateinit var gson: Gson

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
                try {
                    val data = queueRepository.getTopQueue() ?: break

                    val uploadManager = uploadManagerProvider.get(data.uploadType)

                    val uploadResult = uploadManager.execute(
                        data,
                        object : CreationUploadManagerListener {
                            override suspend fun setupForegroundNotification(info: ForegroundInfo) {
                                setForegroundAsync(info)
                            }

                            override suspend fun setProgress(uploadData: CreationUploadData, progress: Int) {
                                emitProgress(progress, uploadData)
                            }
                        }
                    )

                    if (uploadResult) {
                        queueRepository.delete(data.queueId)
                    } else {
                        emitProgress(CreationUploadConst.PROGRESS_FAILED, data)
                        break
                    }
                } catch (throwable: Throwable) {
                    when (throwable) {
                        is JsonSyntaxException,
                        is UnknownUploadTypeException,
                        is NoUploadManagerException -> {
                            queueRepository.deleteTopQueue()
                            continue
                        }
                        else -> {
                            break
                        }
                    }
                }
            }

            Result.success()
        }
    }

    private suspend fun emitProgress(progress: Int, data: CreationUploadData) {
        setProgress(
            workDataOf(
                CreationUploadConst.PROGRESS to progress,
                CreationUploadConst.UPLOAD_DATA to data.mapToJson(gson)
            )
        )
        delay(DEFAULT_DELAY_AFTER_EMIT_PROGRESS)
    }

    companion object {

        private const val DEFAULT_DELAY_AFTER_EMIT_PROGRESS = 1000L
        fun build(): OneTimeWorkRequest {
            return OneTimeWorkRequest.Builder(CreationUploaderWorker::class.java)
                .build()
        }
    }
}
