package com.tokopedia.mediauploader

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.state.ProgressCallback
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.common.util.isImage
import com.tokopedia.mediauploader.common.util.request
import com.tokopedia.mediauploader.image.ImageUploaderManager
import com.tokopedia.mediauploader.video.LargeUploaderManager
import com.tokopedia.mediauploader.video.SimpleUploaderManager
import com.tokopedia.mediauploader.video.VideoUploaderManager
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.Dispatchers
import java.io.File
import javax.inject.Inject

class UploaderUseCase @Inject constructor(
    private val imageUploaderManager: ImageUploaderManager,
    private val videoUploaderManager: VideoUploaderManager
) : CoroutineUseCase<RequestParams, UploadResult>(Dispatchers.IO) {

    private var progressUploader: ProgressCallback? = null

    private lateinit var sourceId: String
    private lateinit var file: File

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

    override suspend fun execute(params: RequestParams): UploadResult {
        sourceId = params.getString(PARAM_SOURCE_ID, "")
        file = params.getObject(PARAM_FILE_PATH) as File

        return if (file.name.isImage()) {
            imageUploader()
        } else {
            videoUploader()
        }
    }

    private suspend fun videoUploader() = request(
        file = file,
        sourceId = sourceId,
        loader = progressUploader,
        uploaderManager = videoUploaderManager,
        execute = { videoUploaderManager(file, sourceId) }
    )

    private suspend fun imageUploader() = request(
        file = file,
        sourceId = sourceId,
        loader = progressUploader,
        uploaderManager = imageUploaderManager,
        execute = { imageUploaderManager(file, sourceId) }
    )

    // Public Method
    fun trackProgress(progress: (percentage: Int) -> Unit) {
        this.progressUploader = object : ProgressCallback {
            override fun onProgress(percentage: Int) {
                progress(percentage)
            }
        }
    }

    // Public Method
    fun createParams(sourceId: String, filePath: File): RequestParams {
        return RequestParams.create().apply {
            putString(PARAM_SOURCE_ID, sourceId)
            putObject(PARAM_FILE_PATH, filePath)
        }
    }

    // Public Method
    suspend fun abortUpload(abort: () -> Unit) {
        try {
            videoUploaderManager.abortUpload { abort() }
        } catch (t: Throwable) {}
    }

    companion object {
        const val PARAM_SOURCE_ID = "source_id"
        const val PARAM_FILE_PATH = "file_path"
    }
}