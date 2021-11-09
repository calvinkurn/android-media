package com.tokopedia.kyc_centralized.domain

import com.tokopedia.kyc_centralized.KycUrl.KYC_PARAMS
import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.data.repository.KycUploadImagesRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class KycUploadUseCase @Inject constructor(private val livenessUploadImagesRepository: KycUploadImagesRepository) {
    suspend fun uploadImages(ktpPath: String, facePath: String, tkpdProjectId: String): KycData {
        val ktpFile = File(ktpPath)
        val faceFile = File(facePath)

        val requestBodyKtp = ktpFile.asRequestBody(IMAGE.toMediaTypeOrNull())
        val requestBodyFace = faceFile.asRequestBody(IMAGE.toMediaTypeOrNull())
        val ktpImage = MultipartBody.Part.createFormData(KTP_IMAGE, ktpFile.name, requestBodyKtp)
        val faceImage =
            MultipartBody.Part.createFormData(FACE_IMAGE, faceFile.name, requestBodyFace)

        val projectId = tkpdProjectId.toRequestBody(TEXT.toMediaTypeOrNull())
        val params = KYC_PARAMS.toRequestBody(TEXT.toMediaTypeOrNull())

        return livenessUploadImagesRepository.uploadImages(
            projectId, params, ktpImage, faceImage, tkpdProjectId
        )
    }

    companion object {
        const val IMAGE = "image/*"
        const val TEXT = "text/plain"
        const val KTP_IMAGE = "ktp_image"
        const val FACE_IMAGE = "face_image"
    }
}
