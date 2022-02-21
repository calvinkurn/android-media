package com.tokopedia.mediauploader.services

import android.content.Context
import androidx.work.*
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.mediauploader.UploaderUseCase
import com.tokopedia.mediauploader.common.di.MediaUploaderModule
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.di.DaggerMediaUploaderTestComponent
import com.tokopedia.mediauploader.di.MediaUploaderTestModule
import com.tokopedia.mediauploader.manager.UploadMediaNotificationManager
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*
import javax.inject.Inject

class UploaderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    @Inject lateinit var lazyUploaderUseCase: Lazy<UploaderUseCase>
    private val uploaderUseCase: UploaderUseCase get() = lazyUploaderUseCase.get()

    @Inject lateinit var lazyNotificationManager: Lazy<UploadMediaNotificationManager>
    private val notificationManager: UploadMediaNotificationManager get() = lazyNotificationManager.get()

    init {
        initInjector()
    }

    override suspend fun doWork(): Result {
        return try {
            trackProgressLoader()
            when (val result = upload()) {
                is UploadResult.Success -> onSuccessUpload(result)
                is UploadResult.Error -> onFailedUpload()
            }
        } catch (t: Throwable) {
            onFailedUpload()
        }
    }

    private suspend fun upload() = withContext(Dispatchers.IO) {
        val uploadParams = uploaderUseCase.createParams(
            getSourceId(),
            getFilePath(),
            isSupportTranscode()
        )

        uploaderUseCase(uploadParams)
    }

    private fun onSuccessUpload(result: UploadResult.Success): Result {
        val outputData = Data.Builder()
            .putString(RESULT_UPLOAD_ID, result.uploadId)
            .putString(RESULT_VIDEO_URL, result.videoUrl)
            .build()

        notificationManager.onSuccess()
        return Result.success(outputData)
    }

    private fun onFailedUpload(): Result {
        notificationManager.onError()
        return Result.failure()
    }

    private fun trackProgressLoader() {
        notificationManager.setId(getNotificationId())
        notificationManager.onStart()

        uploaderUseCase.trackProgress { progress ->
            notificationManager.onProgress(
                sourceId = getSourceId(),
                file = getFilePath(),
                progress = progress
            )
        }
    }

    private fun getSourceId(): String {
        return inputData.getString(PARAM_SOURCE_ID).orEmpty()
    }

    private fun getNotificationId(): Int {
        return inputData.getInt(PARAM_NOTIFICATION_ID, Random().nextInt())
    }

    private fun getFilePath(): File {
        return File(inputData.getString(PARAM_FILE_PATH).orEmpty())
    }

    private fun isSupportTranscode(): Boolean {
        return inputData.getBoolean(PARAM_TRANSCODE_SUPPORTED, true)
    }

    private fun initInjector() {
        DaggerMediaUploaderTestComponent.builder()
            .baseAppComponent((applicationContext as BaseMainApplication).baseAppComponent)
            .mediaUploaderTestModule(MediaUploaderTestModule(applicationContext))
            .mediaUploaderModule(MediaUploaderModule())
            .build()
            .inject(this)
    }

    companion object {
        // work manager name
        const val UNIQUE_WORK_NAME = "media_uploader"

        // input param
        private const val PARAM_NOTIFICATION_ID = "notification_id"
        private const val PARAM_TRANSCODE_SUPPORTED = "transcode_supported"
        private const val PARAM_SOURCE_ID = "source_id"
        private const val PARAM_FILE_PATH = "file_path"

        // output result
        const val RESULT_UPLOAD_ID = "upload_id"
        const val RESULT_VIDEO_URL = "video_url"

        private fun createWorkRequest(
            sourceId: String,
            filePath: String,
            notificationId: Int,
            isSupportTranscode: Boolean
        ): OneTimeWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val inputData = Data.Builder()
                .putString(PARAM_SOURCE_ID, sourceId)
                .putString(PARAM_FILE_PATH, filePath)
                .putBoolean(PARAM_TRANSCODE_SUPPORTED, isSupportTranscode)
                .putInt(PARAM_NOTIFICATION_ID, notificationId)
                .build()

            return OneTimeWorkRequestBuilder<UploaderWorker>()
                .setConstraints(constraints)
                .setInputData(inputData)
                .build()
        }

        fun createChainedWorkRequests(
            context: Context,
            sourceId: String,
            filePath: String,
            isSupportTranscode: Boolean
        ) {
            val notificationId = Random().nextInt()
            val workRequest = createWorkRequest(
                notificationId = notificationId,
                sourceId = sourceId,
                filePath = filePath,
                isSupportTranscode = isSupportTranscode
            )

            WorkManager
                .getInstance(context.applicationContext)
                .beginUniqueWork(
                    UNIQUE_WORK_NAME,
                    ExistingWorkPolicy.KEEP,
                    workRequest
                ).enqueue()
        }

        fun cancelWork(context: Context) {
            WorkManager
                .getInstance(context.applicationContext)
                .cancelUniqueWork(UNIQUE_WORK_NAME)
        }

        fun pruneWork(context: Context) {
            WorkManager
                .getInstance(context.applicationContext)
                .pruneWork()
        }
    }

}