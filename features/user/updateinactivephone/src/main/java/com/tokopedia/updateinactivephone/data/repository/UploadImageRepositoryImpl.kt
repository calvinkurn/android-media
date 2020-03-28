package com.tokopedia.updateinactivephone.data.repository

import android.util.Log
import com.tokopedia.updateinactivephone.data.factory.UploadImageSourceFactory
import com.tokopedia.updateinactivephone.data.model.request.GeneratedHost
import com.tokopedia.updateinactivephone.data.model.request.UploadHostData
import com.tokopedia.updateinactivephone.data.model.request.UploadHostModel
import com.tokopedia.updateinactivephone.data.model.request.UploadImageModel
import com.tokopedia.updateinactivephone.data.model.response.UploadImageData

import javax.inject.Inject

import okhttp3.RequestBody

class UploadImageRepositoryImpl @Inject constructor(private val uploadImageSourceFactory: UploadImageSourceFactory) : UploadImageRepository {

    override suspend fun uploadImage(url: String, params: Map<String, String>, imageFile: RequestBody): UploadImageModel {
//        return uploadImageSourceFactory
//                .createCloudUploadImageDataStore()
//                .uploadImage(
//                        url,
//                        params,
//                        imageFile)
        var uploadImageData = UploadImageData()
        var uploadImageModel = UploadImageModel()
        uploadImageModel.isSuccess = true
        uploadImageModel.uploadImageData = uploadImageData
        uploadImageModel.errorMessage = ""
        uploadImageModel.isResponseSuccess = true

        return uploadImageModel
    }

    override suspend fun getUploadHost(parameters: HashMap<String, Any>): UploadHostModel {
//        return uploadImageSourceFactory
//                .createCloudUploadHostDataStore()
//                .getUploadHost(parameters)
        var uploadHostData = UploadHostData()
        uploadHostData.generatedHost.serverId = 1
        uploadHostData.generatedHost.uploadHost = ""

        var uploadHostModel = UploadHostModel()
        uploadHostModel.isSuccess = true
        uploadHostModel.errorMessage = ""
        uploadHostModel.uploadHostData = uploadHostData

        return uploadHostModel
    }

}
