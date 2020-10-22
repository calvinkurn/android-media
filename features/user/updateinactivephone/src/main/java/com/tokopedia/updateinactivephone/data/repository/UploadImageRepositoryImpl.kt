package com.tokopedia.updateinactivephone.data.repository

import com.tokopedia.updateinactivephone.data.factory.UploadImageSourceFactory
import com.tokopedia.updateinactivephone.data.model.request.UploadHostModel
import com.tokopedia.updateinactivephone.data.model.request.UploadImageModel

import javax.inject.Inject

import okhttp3.RequestBody

class UploadImageRepositoryImpl @Inject constructor(private val uploadImageSourceFactory: UploadImageSourceFactory) : UploadImageRepository {

    override suspend fun uploadImage(url: String, userId: RequestBody, imageFile: RequestBody): UploadImageModel {
        return uploadImageSourceFactory
                .createCloudUploadImageDataStore()
                .uploadImage(
                        url,
                        userId,
                        imageFile)
    }

    override suspend fun getUploadHost(parameters: HashMap<String, Any>): UploadHostModel {
        return uploadImageSourceFactory
                .createCloudUploadHostDataStore()
                .getUploadHost(parameters)
    }

}
