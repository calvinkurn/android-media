package ai.advance.liveness.data.source

import ai.advance.liveness.data.model.response.LivenessData
import ai.advance.liveness.data.network.LivenessDetectionApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import javax.inject.Inject

class LivenessUploadImagesDataSourceCloud @Inject constructor(
    private val retrofit: Retrofit) {
    private var livenessData = LivenessData()

    suspend fun uploadImage(projectId: RequestBody, params: RequestBody, ktpImage: MultipartBody.Part, faceImage: MultipartBody.Part): LivenessData {
        livenessData =  retrofit.create(LivenessDetectionApi::class.java).uploadImages(projectId, params, ktpImage, faceImage).data
        return livenessData
    }
}