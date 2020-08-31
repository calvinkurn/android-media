package com.tokopedia.updateinactivephone.data.source

import com.tokopedia.updateinactivephone.data.mapper.UploadImageMapper
import com.tokopedia.updateinactivephone.data.model.request.UploadImageModel
import com.tokopedia.updateinactivephone.data.network.api.UploadImageApi

import okhttp3.RequestBody
import retrofit2.Retrofit

class CloudUploadImageDataSource(private val retrofit: Retrofit,
                                 private val uploadImageMapper: UploadImageMapper) {
    suspend fun uploadImage(url: String,
                    userId: RequestBody,
                    imageFile: RequestBody): UploadImageModel {
        return uploadImageMapper.call(retrofit.create(UploadImageApi::class.java).uploadImage(url, userId, imageFile))
    }
}
