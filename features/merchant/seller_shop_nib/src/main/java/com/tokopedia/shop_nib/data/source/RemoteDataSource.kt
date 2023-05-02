package com.tokopedia.shop_nib.data.source

import android.net.Uri
import com.tokopedia.shop_nib.data.mapper.UploadFileMapper
import com.tokopedia.shop_nib.data.service.UploadFileService
import com.tokopedia.shop_nib.domain.entity.UploadFileResult
import com.tokopedia.shop_nib.util.FileHelper
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: UploadFileService,
    private val fileHelper: FileHelper,
    private val mapper: UploadFileMapper
) {
    companion object {
        private const val REQUEST_PARAM_KEY_FILE = "file_upload"
    }

    suspend fun uploadFile(fileUri: String): UploadFileResult {
        val uri = Uri.parse(fileUri)

        val fileExtension = fileHelper.getFileExtension(uri)
        val fileName = "file.$fileExtension"
        val file = fileHelper.getFileFromUri(uri, fileName) ?: throw Exception("Failed to create file from URI")


        val fileMime = if (fileExtension == "pdf") {
            "application/pdf"
        } else {
            "image/$fileExtension"
        }

        val fileBody = file.asRequestBody(fileMime.toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData(REQUEST_PARAM_KEY_FILE, file.name, fileBody)

        val response = service.uploadFile(filePart)

        //Remove the file from app cache directory if it was successfully submitted
        if (response.data?.resultStatus?.code == "200") {
            fileHelper.delete(file)
        }

        return mapper.map(response)
    }
}
