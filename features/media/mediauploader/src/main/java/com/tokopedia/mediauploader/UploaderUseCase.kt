@file:Suppress("UNCHECKED_CAST")

package com.tokopedia.mediauploader

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.cache.SourcePolicyManager
import com.tokopedia.mediauploader.common.data.entity.SourcePolicy
import com.tokopedia.mediauploader.common.di.UploaderQualifier
import com.tokopedia.mediauploader.common.state.ProgressType
import com.tokopedia.mediauploader.common.state.ProgressUploader
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.image.ImageUploaderManager
import com.tokopedia.mediauploader.video.VideoUploaderManager
import com.tokopedia.usecase.RequestParams
import java.io.File
import javax.inject.Inject

class UploaderUseCase @Inject constructor(
    @UploaderQualifier private val sourcePolicyManager: SourcePolicyManager,
    private val imageUploaderManager: ImageUploaderManager,
    private val videoUploaderManager: VideoUploaderManager,
    private val sourcePolicyUseCase: GetSourcePolicyUseCase,
    dispatchers: CoroutineDispatchers,
) : CoroutineUseCase<RequestParams, UploadResult>(dispatchers.io) {

    private var progressUploader: ProgressUploader? = null

    override suspend fun execute(params: RequestParams): UploadResult {
        val useCaseParam = setupRequestParams(params)

        val baseParam = useCaseParam.base as BaseParam
        val imageParam = useCaseParam.image as ImageParam

        getOrSetGlobalSourcePolicy(
            file = baseParam.file,
            sourceId = baseParam.sourceId,
            isSecure = imageParam.isSecure
        )

        return UploaderFactory(
            video = videoUploaderManager,
            image = imageUploaderManager
        ).createUploader(useCaseParam).also {
            // clear policy cache after upload succeed
            sourcePolicyManager.dispose()
        }
    }

    private fun setupRequestParams(params: RequestParams): UseCaseParam {
        val base = BaseParam(
            file = params.getObject(PARAM_FILE_PATH) as File,
            sourceId = params.getString(PARAM_SOURCE_ID, ""),
            progress = progressUploader
        )

        return UseCaseParam(
            base = base,
            image = ImageParam(
                isSecure = params.getBoolean(PARAM_IS_SECURE, false),
                extraHeader = params.getObject(PARAM_EXTRA_HEADER) as Map<String, String>,
                extraBody = params.getObject(PARAM_EXTRA_BODY) as Map<String, String>,
                base = base
            ),
            video = VideoParam(
                withTranscode = params.getBoolean(PARAM_WITH_TRANSCODE, true),
                shouldCompress = params.getBoolean(PARAM_SHOULD_COMPRESS, true),
                ableToRetry = params.getBoolean(PARAM_IS_RETRY, false),
                base = base
            )
        )
    }

    /**
     * A new fetcher of source policy.
     *
     * We put all policies request in only method and make it offline-first mode.
     * So we don't have to request the same policy if users encounter internet issue.
     */
    private suspend fun getOrSetGlobalSourcePolicy(sourceId: String, file: File, isSecure: Boolean): SourcePolicy {
        val param = GetSourcePolicyUseCase.Param(sourceId, file, isSecure)
        return sourcePolicyManager.get() ?: sourcePolicyUseCase(param).also {
            sourcePolicyManager.set(it)
        }
    }

    // Public Method
    @Deprecated(
        message = "This method is deprecated due the media-uploader now support to track the video compression state.",
        replaceWith = ReplaceWith("trackProgress(progress: (Int, ProgressType) -> Unit)")
    )
    fun trackProgress(progress: (Int) -> Unit) {
        this.progressUploader = object : ProgressUploader {
            override fun onProgress(percentage: Int, type: ProgressType) {
                progress(percentage)
            }
        }
    }

    fun trackProgress(progress: (Int, ProgressType) -> Unit) {
        this.progressUploader = object : ProgressUploader {
            override fun onProgress(percentage: Int, type: ProgressType) {
                progress(percentage, type)
            }
        }
    }

    // Public Method
    fun createParams(
        sourceId: String,
        filePath: File,
        withTranscode: Boolean = true,
        isSecure: Boolean = false,
        isRetriable: Boolean = false,
        shouldCompress: Boolean = true,
        extraHeader: Map<String, String> = mapOf(),
        extraBody: Map<String, String> = mapOf()
    ): RequestParams {
        return RequestParams.create().apply {
            putBoolean(PARAM_WITH_TRANSCODE, withTranscode)
            putString(PARAM_SOURCE_ID, sourceId)
            putObject(PARAM_FILE_PATH, filePath)
            putBoolean(PARAM_IS_SECURE, isSecure)
            putBoolean(PARAM_IS_RETRY, isRetriable)
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

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

    companion object {
        const val PARAM_SOURCE_ID = "source_id"
        const val PARAM_FILE_PATH = "file_path"
        const val PARAM_WITH_TRANSCODE = "with_transcode"
        const val PARAM_IS_SECURE = "is_secure"
        const val PARAM_EXTRA_HEADER = "extra_header"
        const val PARAM_EXTRA_BODY = "extra_body"
        const val PARAM_SHOULD_COMPRESS = "should_compress"
        const val PARAM_IS_RETRY = "is_retry"
    }
}
