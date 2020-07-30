package com.tokopedia.liveness.data.repository

import com.tokopedia.liveness.data.model.response.LivenessData
import com.tokopedia.liveness.data.source.LivenessUploadImagesDataSource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class LivenessUploadImagesRepository @Inject constructor(private val uploadImageDataSource: LivenessUploadImagesDataSource) {
    suspend fun uploadImages(projectId: RequestBody, params: RequestBody, ktpImage: MultipartBody.Part, faceImage: MultipartBody.Part): LivenessData {
        return uploadImageDataSource.uploadImages(projectId, params, ktpImage, faceImage)
    }
}