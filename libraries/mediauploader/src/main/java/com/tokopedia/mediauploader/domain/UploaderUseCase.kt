package com.tokopedia.mediauploader.domain

import android.graphics.BitmapFactory
import com.tokopedia.mediauploader.data.consts.UrlBuilder
import com.tokopedia.mediauploader.data.entity.Uploader
import com.tokopedia.mediauploader.data.mapper.ImagePolicyMapper
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.UseCase
import java.io.File
import javax.inject.Inject

class UploaderUseCase @Inject constructor(
        private val dataPolicyUseCase: DataPolicyUseCase,
        private val mediaUploaderUseCase: MediaUploaderUseCase
) : UseCase<Uploader>() {

    var requestParams = RequestParams()

    override suspend fun executeOnBackground(): Uploader {
        if (requestParams.parameters.isEmpty()) throw Exception("Not param found")

        val sourceId = requestParams.getString(PARAM_SOURCE_ID, "")
        val filePath = requestParams.getString(PARAM_FILE_PATH, "")
        val fileUpload = File(filePath)

        if (!fileUpload.exists()) return Uploader(false, "File not found")

        //get media upload policy
        dataPolicyUseCase.requestParams = DataPolicyUseCase.createParams(sourceId)
        val policy = dataPolicyUseCase.executeOnBackground()
        val sourcePolicyData = ImagePolicyMapper.mapToSourcePolicy(policy.dataPolicy)

        //validation
        val imagePolicy = sourcePolicyData.imagePolicy

        val maxFileSize = imagePolicy.maxFileSize
        val maxWidth = imagePolicy.maximumRes.width
        val maxHeight = imagePolicy.maximumRes.height
        val minWidth = imagePolicy.minimumRes.width
        val minHeight = imagePolicy.minimumRes.height
        val acceptExtension = imagePolicy.extension.split(",")

        //check file extension
        if (!acceptExtension.contains(getFileExtension(filePath))) {
            return Uploader(false, "File extension is not allowed")
        }

        //max file size
        if (isMaxFileSize(fileUpload, maxFileSize)) {
            return Uploader(false, "File size is over than $maxFileSize")
        }

        //minimum resolution
        if (isMinBitmapResolution(filePath, minWidth, minHeight)) {
            return Uploader(false, "Resolution of bitmap is too tiny")
        }

        //maximum resolution
        if (isMaxBitmapResolution(filePath, maxWidth, maxHeight)) {
            return Uploader(false, "Resolution of bitmap is too big")
        }

        //upload
        val uploadUrl = UrlBuilder.generate(sourcePolicyData.host, sourceId)
        mediaUploaderUseCase.requestParams = MediaUploaderUseCase.createParam(uploadUrl, filePath)
        val upload = mediaUploaderUseCase.executeOnBackground()
        return Uploader(true, "", upload.data.uploadId)
    }

    private fun getFileExtension(filePath: String): String {
        val lastIndexOf = filePath.lastIndexOf(".")
        return if (lastIndexOf == -1) "" else filePath.substring(lastIndexOf)
    }

    private fun isMaxFileSize(file: File, maxFileSize: Int): Boolean {
        return file.length() > maxFileSize
    }

    private fun getBitmapOptions(filePath: String): BitmapFactory.Options {
        val bitmapOptions = BitmapFactory.Options()
        bitmapOptions.inJustDecodeBounds = true
        BitmapFactory.decodeFile(filePath, bitmapOptions)
        return bitmapOptions
    }

    private fun isMaxBitmapResolution(filePath: String, maxWidth: Int, maxHeight: Int): Boolean {
        val bitmapOptions = getBitmapOptions(filePath)
        val width = bitmapOptions.outWidth
        val height = bitmapOptions.outHeight
        return width > maxWidth && height > maxHeight
    }

    private fun isMinBitmapResolution(filePath: String, minWidth: Int, minHeight: Int): Boolean {
        val bitmapOptions = getBitmapOptions(filePath)
        val width = bitmapOptions.outWidth
        val height = bitmapOptions.outHeight
        return width < minWidth && height < minHeight
    }

    companion object {
        const val PARAM_SOURCE_ID = "source_id"
        const val PARAM_FILE_PATH = "file_path"

        fun createParams(sourceId: String, filePath: String): RequestParams {
            val params = RequestParams()
            params.putString(PARAM_SOURCE_ID, sourceId)
            params.putString(PARAM_FILE_PATH, filePath)
            return params
        }
    }

}