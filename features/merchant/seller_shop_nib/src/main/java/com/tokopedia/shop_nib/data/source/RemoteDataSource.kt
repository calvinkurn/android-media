package com.tokopedia.shop_nib.data.source

import android.net.Uri
import com.tokopedia.shop_nib.data.response.UploadFileResponse
import com.tokopedia.shop_nib.data.service.UploadFileService
import com.tokopedia.shop_nib.util.FileHelper
import com.tokopedia.user.session.UserSessionInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val service: UploadFileService,
    private val userSessionInterface: UserSessionInterface,
    private val fileHelper: FileHelper,
) {
    suspend fun uploadFile(fileUri: String): UploadFileResponse {
        val uri = Uri.parse(fileUri)

        val fileExtension = fileHelper.getFileExtension(uri)
        val fileName = "file.$fileExtension"
        val file = fileHelper.getFileFromUri(uri, fileName) ?: throw Exception("File not found")


        val fileMime = if (fileExtension == "pdf") {
            "application/pdf"
        } else {
            "image/$fileExtension"
        }

        val fileBody = RequestBody.create(fileMime.toMediaTypeOrNull(), file)
        val filePart = MultipartBody.Part.createFormData("image", file.name, fileBody)

        val params = HashMap<String, RequestBody>()
        params["user_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(), userSessionInterface.userId)
        params["shop_id"] = RequestBody.create("text/plain".toMediaTypeOrNull(), userSessionInterface.shopId)

        return service.uploadFile(filePart, params)
    }
}
