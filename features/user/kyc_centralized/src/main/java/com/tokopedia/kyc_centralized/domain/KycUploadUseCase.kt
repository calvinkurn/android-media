package com.tokopedia.kyc_centralized.domain

import com.tokopedia.kyc_centralized.KycUrl.KYC_PARAMS
import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.data.repository.KycUploadImagesRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class KycUploadUseCase @Inject constructor(private val livenessUploadImagesRepository: KycUploadImagesRepository) {
    suspend fun uploadImages(ktpPath: String, facePath: String, tkpdProjectId: String): KycData {
        val ktpFile = File(ktpPath)
        val faceFile = File(facePath)

        val requestBodyKtp = RequestBody.create(MediaType.parse(IMAGE), ktpFile)
        val requestBodyFace = RequestBody.create(MediaType.parse(IMAGE), faceFile)
        val ktpImage = MultipartBody.Part.createFormData(KTP_IMAGE, ktpFile.name, requestBodyKtp)
        val faceImage = MultipartBody.Part.createFormData(FACE_IMAGE, faceFile.name, requestBodyFace)

        val projectId = RequestBody.create(MediaType.parse(TEXT), tkpdProjectId)
        val params = RequestBody.create(MediaType.parse(TEXT), KYC_PARAMS)

        return livenessUploadImagesRepository.uploadImages(projectId, params, ktpImage, faceImage)
    }

    companion object {
        const val IMAGE = "image/*"
        const val TEXT = "text/plain"
        const val KTP_IMAGE = "ktp_image"
        const val FACE_IMAGE = "face_image"
    }
}
