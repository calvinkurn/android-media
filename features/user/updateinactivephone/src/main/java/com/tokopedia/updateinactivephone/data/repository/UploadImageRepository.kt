package com.tokopedia.updateinactivephone.data.repository

import com.tokopedia.updateinactivephone.data.model.request.UploadHostModel
import com.tokopedia.updateinactivephone.data.model.request.UploadImageModel

import okhttp3.RequestBody

interface UploadImageRepository {
    suspend fun uploadImage(url: String, params: Map<String, String>, imageFile: RequestBody): UploadImageModel
    suspend fun getUploadHost(parameters: HashMap<String, Any>): UploadHostModel
}
