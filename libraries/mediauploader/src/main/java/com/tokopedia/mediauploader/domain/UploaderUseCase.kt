package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.BaseUseCase
import com.tokopedia.mediauploader.data.consts.UrlBuilder
import com.tokopedia.mediauploader.data.entity.SourcePolicy
import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.data.state.UploadErrorState
import com.tokopedia.mediauploader.util.UploadValidatorUtil.getFileExtension
import com.tokopedia.mediauploader.util.UploadValidatorUtil.isMaxBitmapResolution
import com.tokopedia.mediauploader.util.UploadValidatorUtil.isMaxFileSize
import com.tokopedia.mediauploader.util.UploadValidatorUtil.isMinBitmapResolution
import com.tokopedia.usecase.RequestParams
import java.io.File
import java.net.SocketTimeoutException
import javax.inject.Inject
import com.tokopedia.mediauploader.data.mapper.ImagePolicyMapper.mapToSourcePolicy as map
import com.tokopedia.mediauploader.domain.DataPolicyUseCase.Companion.createParams as policyParam

class UploaderUseCase @Inject constructor(
        private val dataPolicyUseCase: DataPolicyUseCase,
        private val mediaUploaderUseCase: MediaUploaderUseCase
) : BaseUseCase<RequestParams, UploadResult>() {

    private var progressCallback: ProgressCallback? = null

    override suspend fun execute(params: RequestParams): UploadResult {
        if (params.parameters.isEmpty()) throw Exception("Not param found")

        //get params
        val sourceId = params.getString(PARAM_SOURCE_ID, "")
        val fileToUpload = params.getObject(PARAM_FILE_PATH) as File

        return try {
            //get media upload policy
            val sourcePolicy = getMediaPolicy(sourceId)

            //validate it
            validatorPolicy(fileToUpload, sourcePolicy) {

                //uploading
                postMedia(fileToUpload, sourcePolicy, sourceId)
            }
        } catch (e: Throwable) {
            //error comes from media policy request
            UploadResult.Error(UploadErrorState.NETWORK_ERROR)
        }
    }

    /**
     * (get) media policy
     * @return: sourcePolicy
     */
    private suspend fun getMediaPolicy(sourceId: String): SourcePolicy {
        val dataPolicyParams = policyParam(sourceId)
        val policyData = dataPolicyUseCase(dataPolicyParams)
        return map(policyData.dataPolicy)
    }

    /**
     * (post) media uploader
     * @return: uploadId
     */
    private suspend fun postMedia(
            fileToUpload: File,
            sourcePolicy: SourcePolicy,
            sourceId: String
    ): UploadResult {
        //progress of uploader
        mediaUploaderUseCase.progressCallback = progressCallback

        //uploading a media
        val generatedUrl = UrlBuilder.generate(sourcePolicy.host, sourceId)
        val mediaUploaderParams = MediaUploaderUseCase.createParams(
                uploadUrl = generatedUrl,
                filePath = fileToUpload.path
        )

        return try {
            val upload = mediaUploaderUseCase(mediaUploaderParams)
            //get upload id
            UploadResult.Success(upload.data?.uploadId?: "")
        } catch (e: SocketTimeoutException) {
            //time out issue
            UploadResult.Error(UploadErrorState.TIME_OUT)
        } catch (e: Throwable) {
            //global network error state
            UploadResult.Error(UploadErrorState.NETWORK_ERROR)
        }
    }

    /**
     * media validator
     * @return: state
     */
    private suspend fun validatorPolicy(
            fileToUpload: File,
            sourcePolicy: SourcePolicy,
            onUpload: suspend () -> UploadResult
    ): UploadResult {
        //get path from file
        val filePath = fileToUpload.path

        /*
        * get all media policy
        * based on sourceId
        * */
        val maxFileSize = sourcePolicy.imagePolicy.maxFileSize
        val maxWidth = sourcePolicy.imagePolicy.maximumRes.width
        val maxHeight = sourcePolicy.imagePolicy.maximumRes.height
        val minWidth = sourcePolicy.imagePolicy.minimumRes.width
        val minHeight = sourcePolicy.imagePolicy.minimumRes.height
        val acceptExtension = sourcePolicy.imagePolicy
                .extension
                .split(",")

        return when {
            !fileToUpload.exists() -> {
                UploadResult.Error(UploadErrorState.NOT_FOUND)
            }
            !acceptExtension.contains(getFileExtension(filePath)) -> {
                UploadResult.Error(UploadErrorState.EXT_ISSUE)
            }
            isMaxFileSize(filePath, maxFileSize) -> {
                UploadResult.Error(UploadErrorState.MAX_SIZE)
            }
            isMinBitmapResolution(filePath, minWidth, minHeight) -> {
                UploadResult.Error(UploadErrorState.TINY_RES)
            }
            isMaxBitmapResolution(filePath, maxWidth, maxHeight) -> {
                UploadResult.Error(UploadErrorState.LARGE_RES)
            }
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
        val params = RequestParams()
        params.putString(PARAM_SOURCE_ID, sourceId)
        params.putObject(PARAM_FILE_PATH, filePath)
        return params
    }

    companion object {
        /**
         * keys of params
         * @param source_id
         * @param file_path
         */
        const val PARAM_SOURCE_ID = "source_id"
        const val PARAM_FILE_PATH = "file_path"
    }
}