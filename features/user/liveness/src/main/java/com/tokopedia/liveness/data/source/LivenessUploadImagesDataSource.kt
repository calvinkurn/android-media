package com.tokopedia.liveness.data.source

import com.tokopedia.liveness.data.model.response.LivenessData
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class LivenessUploadImagesDataSource @Inject constructor(private val uploadImageDataSourceCloud: LivenessUploadImagesDataSourceCloud) {
    suspend fun uploadImages(projectId: RequestBody, params: RequestBody, ktpImage: MultipartBody.Part, faceImage: MultipartBody.Part): LivenessData {
        return uploadImageDataSourceCloud.uploadImage(projectId, params, ktpImage, faceImage)
    }
}