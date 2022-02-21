package com.tokopedia.mediauploader

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.isVideoFormat
import com.tokopedia.mediauploader.common.util.request
import com.tokopedia.mediauploader.image.ImageUploaderManager
import com.tokopedia.mediauploader.video.VideoUploaderManager
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Inject

class UploaderUseCase @Inject constructor(
    private val imageUploaderManager: ImageUploaderManager,
    private val videoUploaderManager: VideoUploaderManager
) : CoroutineUseCase<RequestParams, UploadResult>(Dispatchers.IO) {

    private var progressUploader: ProgressUploader? = null

    private lateinit var sourceId: String
    private lateinit var file: File
    private var withTranscode = true

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

    override suspend fun execute(params: RequestParams): UploadResult {
        withTranscode = params.getBoolean(PARAM_WITH_TRANSCODE, true)
        sourceId = params.getString(PARAM_SOURCE_ID, "")
        file = params.getObject(PARAM_FILE_PATH) as File

        return if (isVideoFormat(file.absolutePath)) {
            videoUploader(withTranscode)
        } else {
            imageUploader()
        }
    }

    private suspend fun videoUploader(withTranscode: Boolean) = request(
        file = file,
        sourceId = sourceId,
        uploaderManager = videoUploaderManager,
        execute = {
            videoUploaderManager(
                file,
                sourceId,
                progressUploader,
                withTranscode
            )
        }
    )

    private suspend fun imageUploader() = request(
        file = file,
        sourceId = sourceId,
        uploaderManager = imageUploaderManager,
        execute = {
            imageUploaderManager(
                file,
                sourceId,
                progressUploader
            )
        }
    )

    // Public Method
    fun trackProgress(progress: (percentage: Int) -> Unit) {
        this.progressUploader = object : ProgressUploader {
            override fun onProgress(percentage: Int) {
                progress(percentage)
            }
        }
    }

    // Public Method
    fun createParams(
        sourceId: String,
        filePath: File,
        withTranscode: Boolean = true
    ): RequestParams {
        return RequestParams.create().apply {
            putBoolean(PARAM_WITH_TRANSCODE, withTranscode)
            putString(PARAM_SOURCE_ID, sourceId)
            putObject(PARAM_FILE_PATH, filePath)
        }
    }

    // Public Method
    suspend fun abortUpload(
        sourceId: String,
        filePath: String,
        abort: suspend () -> Unit = {}
    ) {
        try {
            val file = File(filePath)
            videoUploaderManager.abortUpload(sourceId, file.name) {
                abort()
            }
        } catch (t: Throwable) {}
    }

    companion object {
        const val PARAM_SOURCE_ID = "source_id"
        const val PARAM_FILE_PATH = "file_path"
        const val PARAM_WITH_TRANSCODE = "with_transcode"
    }
}