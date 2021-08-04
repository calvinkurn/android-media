package com.tokopedia.mediauploader.domain

import android.util.Log
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.mediauploader.UploaderManager
import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.util.trackToTimber
import com.tokopedia.usecase.RequestParams
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.internal.http2.ConnectionShutdownException
import java.io.File
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException
import javax.inject.Inject

class UploaderUseCase @Inject constructor(
        private val dataPolicyUseCase: DataPolicyUseCase,
        private val mediaUploaderUseCase: MediaUploaderUseCase,
        coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CoroutineUseCase<RequestParams, UploadResult>(coroutineDispatcher) {

    private val uploaderManager by lazy {
        UploaderManager(dataPolicyUseCase, mediaUploaderUseCase)
    }

    private var progressCallback: ProgressCallback? = null

    // this domain isn't using graphql service
    override fun graphqlQuery() = ""

    override suspend fun execute(params: RequestParams): UploadResult {
        val sourceId = params.getString(PARAM_SOURCE_ID, "")
        val fileToUpload = params.getObject(PARAM_FILE_PATH) as File

        return try {
            uploaderManager.validate(fileToUpload, sourceId) { sourcePolicy ->
                // track progress bar
                mediaUploaderUseCase.progressCallback = progressCallback

                // upload file
                uploaderManager.post(fileToUpload, sourceId, sourcePolicy)
            }
        } catch (e: Exception) {
            if (e !is UnknownHostException &&
                    e !is SocketException &&
                    e !is InterruptedIOException &&
                    e !is ConnectionShutdownException &&
                    e !is CancellationException) {
                trackToTimber(sourceId, Log.getStackTraceString(e).take(ERROR_MAX_LENGTH).trim())
            }
            // check whether media source is valid
            return uploaderManager.setError(listOf(NETWORK_ERROR), sourceId, fileToUpload)
        }
    }

    fun trackProgress(progress: (percentage: Int) -> Unit) {
        this.progressCallback = object : ProgressCallback {
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
        const val ERROR_MAX_LENGTH = 1500

        // key of params
        const val PARAM_SOURCE_ID = "source_id"
        const val PARAM_FILE_PATH = "file_path"

        // const local error message
        const val NETWORK_ERROR = "Oops, ada gangguan yang perlu kami bereskan. Refresh atau balik lagi nanti."
    }
}