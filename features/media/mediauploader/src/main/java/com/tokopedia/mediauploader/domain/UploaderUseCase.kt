package com.tokopedia.mediauploader.domain

import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.data.consts.NETWORK_ERROR
import com.tokopedia.mediauploader.data.consts.TIMEOUT_ERROR
import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.util.ERROR_MAX_LENGTH
import com.tokopedia.mediauploader.util.trackToTimber
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
        dataPolicyUseCase: DataPolicyUseCase,
        mediaUploaderUseCase: MediaUploaderUseCase
) : CoroutineUseCase<RequestParams, UploadResult>(Dispatchers.IO) {

    private val uploaderManager = UploaderManager(
        dataPolicyUseCase,
        mediaUploaderUseCase
    )

    private var progressUploader: ProgressCallback? = null

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

    override suspend fun execute(params: RequestParams): UploadResult {
        val sourceId = params.getString(PARAM_SOURCE_ID, "")
        val fileToUpload = params.getObject(PARAM_FILE_PATH) as File

        return try {
            uploaderManager.validate(fileToUpload, sourceId) { sourcePolicy ->
                // track progress bar
                uploaderManager.setProgressUploader(progressUploader)

                // upload file
                uploaderManager.post(fileToUpload, sourceId, sourcePolicy)
            }.also { result ->
                if (result is UploadResult.Error) {
                    uploaderManager.setError(listOf(result.message), sourceId, fileToUpload)
                }
            }
        } catch (e: SocketTimeoutException) {
            uploaderManager.setError(listOf(TIMEOUT_ERROR), sourceId, fileToUpload)
        } catch (e: StreamResetException) {
            uploaderManager.setError(listOf(TIMEOUT_ERROR), sourceId, fileToUpload)
        } catch (e: Exception) {
            if (e !is UnknownHostException &&
                e !is SocketException &&
                e !is InterruptedIOException &&
                e !is ConnectionShutdownException &&
                e !is CancellationException) {

                @Suppress("UselessCallOnNotNull")
                if (getStackTraceMessage(e).orEmpty().isNotEmpty()) {
                    trackToTimber(sourceId, getStackTraceMessage(e).take(ERROR_MAX_LENGTH).trim())
                }
            }
            return uploaderManager.setError(listOf(NETWORK_ERROR), sourceId, fileToUpload)
        }
    }

    fun trackProgress(progress: (percentage: Int) -> Unit) {
        this.progressUploader = object : ProgressCallback {
            override fun onProgress(percentage: Int) {
                progress(percentage)
            }
        }
    }

    fun createParams(sourceId: String, filePath: File): RequestParams {
        return RequestParams.create().apply {
            putString(PARAM_SOURCE_ID, sourceId)
            putObject(PARAM_FILE_PATH, filePath)
        }
    }

    companion object {
        // key of params
        const val PARAM_SOURCE_ID = "source_id"
        const val PARAM_FILE_PATH = "file_path"
    }
}