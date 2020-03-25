package com.tokopedia.updateinactivephone.data.source

import com.tokopedia.updateinactivephone.data.mapper.UploadImageMapper
import com.tokopedia.updateinactivephone.data.network.service.UploadImageService
import com.tokopedia.updateinactivephone.data.model.request.UploadImageModel

import okhttp3.RequestBody

class CloudUploadImageDataSource(private val uploadImageService: UploadImageService,
                                 private val uploadImageMapper: UploadImageMapper) {
    suspend fun uploadImage(url: String,
                    params: Map<String, String>,
                    imageFile: RequestBody): UploadImageModel {
        return uploadImageMapper.call(uploadImageService.api.uploadImage(url, params, imageFile))
    }
}
