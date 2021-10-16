package com.tokopedia.mediauploader

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.common.data.consts.NETWORK_ERROR
import com.tokopedia.mediauploader.common.data.consts.TIMEOUT_ERROR
import com.tokopedia.mediauploader.common.logger.ERROR_MAX_LENGTH
import com.tokopedia.mediauploader.common.logger.trackToTimber
import com.tokopedia.mediauploader.common.state.ProgressCallback
import com.tokopedia.mediauploader.common.state.UploadResult
import com.tokopedia.mediauploader.image.ImageUploaderManager
import com.tokopedia.mediauploader.image.domain.GetImagePolicyUseCase
import com.tokopedia.mediauploader.image.domain.GetImageUploaderUseCase
import com.tokopedia.mediauploader.video.VideoUploaderManager
import com.tokopedia.mediauploader.video.domain.*
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import okhttp3.internal.http2.ConnectionShutdownException
import okhttp3.internal.http2.StreamResetException
import java.io.File
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import android.util.Log.getStackTraceString as getStackTraceMessage

class UploaderUseCase @Inject constructor(
    imagePolicyUseCase: GetImagePolicyUseCase,
    imageUploaderUseCase: GetImageUploaderUseCase,
    policyUseCase: GetVideoPolicyUseCase,
    simpleUseCase: GetSimpleUploaderUseCase,
    initUseCase: InitVideoUploaderUseCase,
    checkerUseCase: GetChunkCheckerUseCase,
    uploaderUseCase: GetChunkUploaderUseCase,
    completeUseCase: SetCompleteUploaderUseCase,
    abortUseCase: SetAbortUploaderUseCase
) : CoroutineUseCase<RequestParams, UploadResult>(Dispatchers.IO) {

    private val imageUploader = ImageUploaderManager(
        imagePolicyUseCase,
        imageUploaderUseCase
    )

    private val videoUploader = VideoUploaderManager(
        policyUseCase,
        simpleUseCase,
        initUseCase,
        checkerUseCase,
        uploaderUseCase,
        completeUseCase,
        abortUseCase
    )

    private var progressUploader: ProgressCallback? = null

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

    override suspend fun execute(params: RequestParams): UploadResult {
        val sourceId = params.getString(PARAM_SOURCE_ID, "")
        val file = params.getObject(PARAM_FILE_PATH) as File

        return imageUploader(file, sourceId)
    }

    private suspend fun imageUploader(file: File, sourceId: String): UploadResult {
        return try {
            imageUploader(file, sourceId, progressUploader)
        } catch (e: SocketTimeoutException) {
            imageUploader.setError(listOf(TIMEOUT_ERROR), sourceId, file)
        } catch (e: StreamResetException) {
            imageUploader.setError(listOf(TIMEOUT_ERROR), sourceId, file)
        } catch (e: Exception) {
            if (e !is UnknownHostException &&
                e !is SocketException &&
                e !is InterruptedIOException &&
                e !is ConnectionShutdownException &&
                e !is CancellationException
            ) {

                @Suppress("UselessCallOnNotNull")
                if (getStackTraceMessage(e).orEmpty().isNotEmpty()) {
                    trackToTimber(sourceId, getStackTraceMessage(e).take(ERROR_MAX_LENGTH).trim())
                }
            }
            return imageUploader.setError(listOf(NETWORK_ERROR), sourceId, file)
        }
    }

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

    companion object {
        const val PARAM_SOURCE_ID = "source_id"
        const val PARAM_FILE_PATH = "file_path"
    }
}