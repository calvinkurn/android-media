@file:Suppress("UNCHECKED_CAST")

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
    private var shouldCompress = false
    private var isSecure = false
    private var extraHeader: Map<String, String> = mapOf()
    private var extraBody: Map<String, String> = mapOf()

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

    override suspend fun execute(params: RequestParams): UploadResult {
        withTranscode = params.getBoolean(PARAM_WITH_TRANSCODE, true)
        shouldCompress = params.getBoolean(PARAM_SHOULD_COMPRESS, false)
        sourceId = params.getString(PARAM_SOURCE_ID, "")
        file = params.getObject(PARAM_FILE_PATH) as File
        isSecure = params.getBoolean(PARAM_IS_SECURE, false)
        extraHeader = params.getObject(PARAM_EXTRA_HEADER) as Map<String, String>
        extraBody = params.getObject(PARAM_EXTRA_BODY) as Map<String, String>

        return if (isVideoFormat(file.absolutePath)) {
            videoUploader()
        } else {
            imageUploader()
        }
    }

    private suspend fun videoUploader() = request(
        file = file,
        sourceId = sourceId,
        uploaderManager = videoUploaderManager,
        execute = {
            videoUploaderManager(
                file,
                sourceId,
                progressUploader,
                withTranscode,
                shouldCompress
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
                progressUploader,
                isSecure,
                extraHeader = extraHeader,
                extraBody = extraBody
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
        withTranscode: Boolean = true,
        isSecure: Boolean = false,
        shouldCompress: Boolean = false,
        extraHeader: Map<String, String> = mapOf(),
        extraBody: Map<String, String> = mapOf()
    ): RequestParams {
        return RequestParams.create().apply {
            putBoolean(PARAM_WITH_TRANSCODE, withTranscode)
            putString(PARAM_SOURCE_ID, sourceId)
            putObject(PARAM_FILE_PATH, filePath)
            putBoolean(PARAM_IS_SECURE, isSecure)
            putBoolean(PARAM_SHOULD_COMPRESS, shouldCompress)
            putObject(PARAM_EXTRA_HEADER, extraHeader)
            putObject(PARAM_EXTRA_BODY, extraBody)
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
        } catch (ignored: Throwable) {}
    }

    companion object {
        const val PARAM_SOURCE_ID = "source_id"
        const val PARAM_FILE_PATH = "file_path"
        const val PARAM_WITH_TRANSCODE = "with_transcode"
        const val PARAM_IS_SECURE = "is_secure"
        const val PARAM_EXTRA_HEADER = "extra_header"
        const val PARAM_EXTRA_BODY = "extra_body"
        const val PARAM_SHOULD_COMPRESS = "should_compress"
    }
}
