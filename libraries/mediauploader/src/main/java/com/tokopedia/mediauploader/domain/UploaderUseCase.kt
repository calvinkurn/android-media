package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.base.BaseUseCase
import com.tokopedia.mediauploader.data.entity.SourcePolicy
import com.tokopedia.mediauploader.data.mapper.ImagePolicyMapper.mapToSourcePolicy
import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.util.UploadValidatorUtil.getFileExtension
import com.tokopedia.usecase.RequestParams
import okhttp3.internal.http2.StreamResetException
import java.io.File
import java.net.SocketTimeoutException
import javax.inject.Inject
import com.tokopedia.mediauploader.data.consts.UrlBuilder.generate as urlBuilder
import com.tokopedia.mediauploader.domain.DataPolicyUseCase.Companion.createParams as policyParam
import com.tokopedia.mediauploader.domain.MediaUploaderUseCase.Companion.createParams as mediaUploaderParams

class UploaderUseCase @Inject constructor(
        private val dataPolicyUseCase: DataPolicyUseCase,
        private val mediaUploaderUseCase: MediaUploaderUseCase
) : BaseUseCase<RequestParams, UploadResult>() {

    private var progressCallback: ProgressCallback? = null

    override suspend fun execute(params: RequestParams): UploadResult {
        if (params.parameters.isEmpty()) throw Exception("Not param found")

        val sourceId = params.getString(PARAM_SOURCE_ID, "")
        val fileToUpload = params.getObject(PARAM_FILE_PATH) as File

        return try {
            val sourcePolicy = mediaPolicy(sourceId)
            preValidation(fileToUpload, sourcePolicy) {
                postMedia(fileToUpload, sourcePolicy, sourceId)
            }
        } catch (e: SocketTimeoutException) {
            UploadResult.Error(TIMEOUT_ERROR)
        } catch (e: StreamResetException) {
            UploadResult.Error(TIMEOUT_ERROR)
        } catch (e: Exception) {
            UploadResult.Error(NETWORK_ERROR)
        }
    }

    private suspend fun mediaPolicy(sourceId: String): SourcePolicy {
        val dataPolicyParams = policyParam(sourceId)
        val policyData = dataPolicyUseCase(dataPolicyParams)
        return mapToSourcePolicy(policyData.dataPolicy)
    }

    private suspend fun postMedia(
            fileToUpload: File,
            policy: SourcePolicy,
            sourceId: String
    ): UploadResult {
        // progress of uploader
        mediaUploaderUseCase.progressCallback = progressCallback

        // media uploader
        val upload = mediaUploaderUseCase(mediaUploaderParams(
                uploadUrl = urlBuilder(policy.host, sourceId),
                filePath = fileToUpload.path,
                timeOut = policy.timeOut.toString()
        ))

        return upload.data?.let {
            UploadResult.Success(it.uploadId)
        }?: UploadResult.Error(
                upload.header.messages.first()
        )
    }

    private suspend fun preValidation(
            fileToUpload: File,
            sourcePolicy: SourcePolicy,
            onUpload: suspend () -> UploadResult
    ): UploadResult {
        val filePath = fileToUpload.path // file full path
        val extensions = sourcePolicy.imagePolicy
                .extension
                .split(",")

        return when {
            !fileToUpload.exists() -> UploadResult.Error(FILE_NOT_FOUND)
            !extensions.contains(getFileExtension(filePath)) -> UploadResult.Error(
                    "Format file: ${sourcePolicy.imagePolicy.extension}"
            )
            else -> onUpload()
        }
    }

    /**
     * track progress of uploader
     */
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
        // key of params
        const val PARAM_SOURCE_ID = "source_id"
        const val PARAM_FILE_PATH = "file_path"

        // const local error message
        const val TIMEOUT_ERROR = "Request timeout, silakan coba kembali beberapa saat lagi"
        const val NETWORK_ERROR = "Oops, ada gangguan yang perlu kami bereskan. Refresh atau balik lagi nanti."
        const val FILE_NOT_FOUND = "Oops, file tidak ditemukan."
    }
}