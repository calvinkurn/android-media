package ai.advance.liveness.domain

import ai.advance.liveness.data.model.response.LivenessData
import ai.advance.liveness.data.repository.LivenessUploadImagesRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UploadLivenessResultUseCase @Inject constructor(private val livenessUploadImagesRepository: LivenessUploadImagesRepository) {
    suspend fun uploadImages(ktpPath: String, facePath: String, tkpdProjectId: String): LivenessData?{
        val ktpFile = File(ktpPath)
        val faceFile = File(facePath)

        val requestBodyKtp = RequestBody.create(MediaType.parse("image/*"), ktpFile)
        val requestBodyFace = RequestBody.create(MediaType.parse("image/*"), faceFile)
        val ktpImage = MultipartBody.Part.createFormData("ktp_image", ktpFile.name, requestBodyKtp)
        val faceImage = MultipartBody.Part.createFormData("face_image", faceFile.name, requestBodyFace)

        val projectId = RequestBody.create(MediaType.parse("text/plain"), tkpdProjectId)
        val params = RequestBody.create(MediaType.parse("text/plain"), "[{\"kyc_type\": 1,\"param\": \"ktp_image\"},{\"kyc_type\": 2,\"param\": \"face_image\"}]\n")

        return livenessUploadImagesRepository.uploadImages(projectId, params, ktpImage, faceImage)
    }
}
