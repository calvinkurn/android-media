package com.tokopedia.shop_nib.data.source

import android.net.Uri
import com.google.gson.Gson
import com.tokopedia.shop_nib.data.mapper.UploadFileMapper
import com.tokopedia.shop_nib.data.response.UploadFileResponse
import com.tokopedia.shop_nib.data.service.UploadFileService
import com.tokopedia.shop_nib.domain.entity.UploadFileResult
import com.tokopedia.shop_nib.util.helper.FileHelper
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: UploadFileService,
    private val fileHelper: FileHelper,
    private val mapper: UploadFileMapper,
    private val userSession: UserSessionInterface
) {
    companion object {
        private const val REQUEST_PARAM_KEY_FILE = "file_upload"
        private const val REQUEST_PARAM_KEY_RELEASE_DATE = "release_date"
    }

    suspend fun uploadFile(fileUri: String, nibPublishDate: String): UploadFileResult {
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

        val params = HashMap<String, RequestBody>()
        params[REQUEST_PARAM_KEY_RELEASE_DATE] = nibPublishDate.toRequestBody("text/plain".toMediaTypeOrNull())

        val authToken = "Bearer ${userSession.accessToken}"

        val response = service.uploadFile(authToken, filePart, params)

        val isSuccess = response.code() == 200
        val responseBody = response.body() ?: UploadFileResponse()

        return if (isSuccess) {
            //Remove the file from app cache directory if it was successfully submitted
            fileHelper.delete(file)

            mapper.map(responseBody)
        } else {
            val errorMessage = response.toErrorMessage()

            UploadFileResult(isSuccess = false, errorMessage = errorMessage)
        }
    }

    private fun Response<UploadFileResponse>.toErrorMessage(): String {
        val gson = Gson()
        val errorResponse: UploadFileResponse = gson.fromJson(
            this.errorBody()?.charStream()?.readText(),
            UploadFileResponse::class.java
        )
        return errorResponse.header.reason
    }
}
