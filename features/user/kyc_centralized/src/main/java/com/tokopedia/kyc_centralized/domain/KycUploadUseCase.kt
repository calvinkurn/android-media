package com.tokopedia.kyc_centralized.domain

import com.tokopedia.kyc_centralized.common.KycUrl.KYC_PARAMS
import com.tokopedia.kyc_centralized.data.model.KycResponse
import com.tokopedia.kyc_centralized.data.network.KycUploadImagesRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class KycUploadUseCase @Inject constructor(private val livenessUploadImagesRepository: KycUploadImagesRepository) {
    suspend fun uploadImages(ktpPath: String, facePath: String, tkpdProjectId: String, isLiveness: Boolean): KycResponse {
        Timber.d("uploadProjectId=$tkpdProjectId")
        val ktpFile = File(ktpPath)
        val faceFile = File(facePath)

        val requestBodyKtp = ktpFile.asRequestBody(IMAGE.toMediaTypeOrNull())
        val requestBodyFace = faceFile.asRequestBody(IMAGE.toMediaTypeOrNull())
        val ktpImage = MultipartBody.Part.createFormData(KTP_IMAGE, ktpFile.name, requestBodyKtp)
        val faceImage =
            MultipartBody.Part.createFormData(FACE_IMAGE, faceFile.name, requestBodyFace)

        val projectId = tkpdProjectId.toRequestBody(TEXT.toMediaTypeOrNull())
        val params = KYC_PARAMS.toRequestBody(TEXT.toMediaTypeOrNull())

        val kycMode = if (isLiveness) { LIVENESS }
        else { SELFIE }
        val selfieMode = kycMode.toRequestBody(TEXT.toMediaTypeOrNull())

        return livenessUploadImagesRepository.uploadImages(
            projectId, params, ktpImage, faceImage, selfieMode
        )
    }

    companion object {
        const val IMAGE = "image/*"
        const val TEXT = "text/plain"
        const val KTP_IMAGE = "ktp_image"
        const val FACE_IMAGE = "face_image"

        const val LIVENESS = "LIVENESS"
        const val SELFIE = "SELFIE"
    }
}
