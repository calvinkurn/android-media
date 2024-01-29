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
import com.tokopedia.creation.common.upload.model.CreationUploadStatus
import com.tokopedia.creation.common.upload.model.exception.UnknownUploadTypeException
import com.tokopedia.creation.common.upload.uploader.manager.CreationUploadExecutionResult
import com.tokopedia.creation.common.upload.uploader.manager.CreationUploadManagerListener
import com.tokopedia.creation.common.upload.util.logger.CreationUploadLogger
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
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

    @Inject
    lateinit var logger: CreationUploadLogger

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

            val coroutineDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

            val notificationId = (0..10000000).random()

            while(true) {
                try {
                    val data = queueRepository.getTopQueue() ?: break

                    val uploadManager = uploadManagerProvider.get(data)

                    uploadManager.setupManager(
                        object : CreationUploadManagerListener {
                            override fun setupForegroundNotification(info: ForegroundInfo) {
                                setForegroundAsync(info)
                            }

                            override fun setProgress(
                                uploadData: CreationUploadData,
                                progress: Int,
                                uploadStatus: CreationUploadStatus,
                            ) {
                                launch(coroutineDispatcher) {
                                    saveProgress(progress, uploadData, uploadStatus)
                                }
                            }
                        }
                    )

                    val result = uploadManager.execute(notificationId)

                    when (result) {
                        is CreationUploadExecutionResult.Success -> {
                            withContext(coroutineDispatcher) {
                                queueRepository.delete(data.queueId)
                            }
                        }
                        is CreationUploadExecutionResult.Error -> {
                            logger.sendLog(result.uploadData, result.throwable)

                            saveProgress(CreationUploadConst.PROGRESS_FAILED, data, CreationUploadStatus.Failed)
                            break
                        }
                    }
                } catch (throwable: Throwable) {
                    logger.sendLog(throwable)

                    when (throwable) {
                        is JsonSyntaxException,
                        is UnknownUploadTypeException -> {
                            queueRepository.deleteTopQueue()
                            continue
                        }
                        else -> {
                            break
                        }
                    }
                }
            }

            delay(DEFAULT_DELAY_AFTER_EMIT_RESULT)
            Result.success()
        }
    }

    private suspend fun saveProgress(
        progress: Int,
        data: CreationUploadData,
        uploadStatus: CreationUploadStatus,
    ) {
        queueRepository.updateProgress(data.queueId, progress, uploadStatus.value)
    }

    companion object {

        private const val DEFAULT_DELAY_AFTER_EMIT_RESULT = 1000L

        fun build(): OneTimeWorkRequest {
            return OneTimeWorkRequest.Builder(CreationUploaderWorker::class.java)
                .build()
        }
    }
}
