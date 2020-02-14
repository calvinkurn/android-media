package com.tokopedia.mediauploader.domain

import com.tokopedia.mediauploader.BaseUseCase
import com.tokopedia.mediauploader.data.consts.UrlBuilder
import com.tokopedia.mediauploader.data.mapper.ImagePolicyMapper
import com.tokopedia.mediauploader.data.state.ProgressCallback
import com.tokopedia.mediauploader.data.state.UploadResult
import com.tokopedia.mediauploader.data.state.UploadState
import com.tokopedia.mediauploader.util.UploadValidatorUtil.getFileExtension
import com.tokopedia.mediauploader.util.UploadValidatorUtil.isMaxBitmapResolution
import com.tokopedia.mediauploader.util.UploadValidatorUtil.isMaxFileSize
import com.tokopedia.mediauploader.util.UploadValidatorUtil.isMinBitmapResolution
import com.tokopedia.usecase.RequestParams
import java.io.File
import javax.inject.Inject

class UploaderUseCase @Inject constructor(
        private val dataPolicyUseCase: DataPolicyUseCase,
        private val mediaUploaderUseCase: MediaUploaderUseCase
) : BaseUseCase<RequestParams, UploadResult>() {

    private var progressCallback: ProgressCallback? = null

    override suspend fun execute(params: RequestParams): UploadResult {
        if (params.parameters.isEmpty()) throw Exception("Not param found")
        val sourceId = params.getString(PARAM_SOURCE_ID, "")
        val filePath = params.getString(PARAM_FILE_PATH, "")

        //get media upload policy
        val dataPolicyParams = dataPolicyUseCase.createParams(sourceId)
        val policyData = dataPolicyUseCase(dataPolicyParams)
        val policyDataMapper = ImagePolicyMapper.mapToSourcePolicy(policyData.dataPolicy)

        //validator
        val maxFileSize = policyDataMapper.imagePolicy.maxFileSize
        val maxWidth = policyDataMapper.imagePolicy.maximumRes.width
        val maxHeight = policyDataMapper.imagePolicy.maximumRes.height
        val minWidth = policyDataMapper.imagePolicy.minimumRes.width
        val minHeight = policyDataMapper.imagePolicy.minimumRes.height
        val acceptExtension = policyDataMapper.imagePolicy.extension.split(",")

        return when {
            !File(filePath).exists() -> {
                UploadResult.Error(UploadState.NOT_FOUND)
            }
            !acceptExtension.contains(getFileExtension(filePath)) -> {
                UploadResult.Error(UploadState.EXT_NOT_ALLOWED)
            }
            isMaxFileSize(filePath, maxFileSize) -> {
                UploadResult.Error(UploadState.FILE_MAX_SIZE)
            }
            isMinBitmapResolution(filePath, minWidth, minHeight) -> {
                UploadResult.Error(UploadState.TINY_RESOLUTION)
            }
            isMaxBitmapResolution(filePath, maxWidth, maxHeight) -> {
                UploadResult.Error(UploadState.BIG_RESOLUTION)
            }
            else -> {
                //progress of uploader
                mediaUploaderUseCase.progressCallback = progressCallback

                //uploading a media
                val generatedUrl = UrlBuilder.generate(policyDataMapper.host, sourceId)
                val mediaUploaderParams = mediaUploaderUseCase.createParams(generatedUrl, filePath)
                val upload = mediaUploaderUseCase(mediaUploaderParams)

                //get upload id
                return UploadResult.Success(upload.data?.uploadId?: "")
            }
        }
    }

    fun trackProgress(test: (percentage: Int) -> Unit) {
        this.progressCallback = object : ProgressCallback {
            override fun onProgress(percentage: Int) {
                test(percentage)
            }
        }
    }

    fun createParams(sourceId: String, filePath: String): RequestParams {
        val params = RequestParams()
        params.putString(PARAM_SOURCE_ID, sourceId)
        params.putString(PARAM_FILE_PATH, filePath)
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