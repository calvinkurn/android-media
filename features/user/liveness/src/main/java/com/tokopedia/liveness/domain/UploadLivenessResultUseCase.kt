package com.tokopedia.liveness.domain

import com.tokopedia.liveness.data.model.response.LivenessData
import com.tokopedia.liveness.data.repository.LivenessUploadImagesRepository
import com.tokopedia.liveness.utils.LivenessConstants
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class UploadLivenessResultUseCase @Inject constructor(private val livenessUploadImagesRepository: LivenessUploadImagesRepository) {
    suspend fun uploadImages(ktpPath: String, facePath: String, tkpdProjectId: String): LivenessData{
        val ktpFile = File(ktpPath)
        val faceFile = File(facePath)

        val requestBodyKtp = RequestBody.create(MediaType.parse(IMAGE), ktpFile)
        val requestBodyFace = RequestBody.create(MediaType.parse(IMAGE), faceFile)
        val ktpImage = MultipartBody.Part.createFormData(KTP_IMAGE, ktpFile.name, requestBodyKtp)
        val faceImage = MultipartBody.Part.createFormData(FACE_IMAGE, faceFile.name, requestBodyFace)

        val projectId = RequestBody.create(MediaType.parse(TEXT), tkpdProjectId)
        val params = RequestBody.create(MediaType.parse(TEXT), LivenessConstants.KYC_PARAMS)

        return livenessUploadImagesRepository.uploadImages(projectId, params, ktpImage, faceImage)
    }

    companion object {
        const val IMAGE = "image/*"
        const val TEXT = "text/plain"
        const val KTP_IMAGE = "ktp_image"
        const val FACE_IMAGE = "face_image"
    }
}
