package com.tokopedia.kyc_centralized.fakes

import com.tokopedia.kyc_centralized.data.model.KycAppModel
import com.tokopedia.kyc_centralized.data.model.KycData
import com.tokopedia.kyc_centralized.data.model.KycResponse
import com.tokopedia.kyc_centralized.data.network.KycUploadApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.http2.ErrorCode
import okhttp3.internal.http2.StreamResetException
import okio.Buffer
import timber.log.Timber

class FakeKycUploadApi(var case: Case = Case.Success) : KycUploadApi {

    var uploadCount = 0

    override suspend fun uploadImages(
        projectId: RequestBody,
        params: RequestBody,
        ktpImage: MultipartBody.Part,
        faceImage: MultipartBody.Part,
        selfieMode: RequestBody
    ): KycResponse {
        Timber.d("uploadFaceImage=${convertRequestBodyToString(selfieMode)}")
        return when (case) {
            Case.Success -> KycResponse(data = KycData(true))
            Case.NetworkFailed -> {
                if (uploadCount == 0) {
                    uploadCount += 1
                    throw StreamResetException(ErrorCode.CANCEL)
                } else {
                    KycResponse(
                        data = KycData(
                            isSuccessRegister = true,
                        )
                    )
                }
            }
            is Case.Retake -> {
                if (uploadCount == 0) {
                    uploadCount += 1
                    KycResponse(
                        data = KycData(
                            isSuccessRegister = false,
                            listRetake = (case as Case.Retake).code,
                            app = KycAppModel(
                                title = "Verifikasi Wajah tidak berhasil",
                                subtitle = "di tempat terang, ya!",
                                button = "Verifikasi Ulang"
                            )
                        )
                    )
                } else {
                    KycResponse(data = KycData(true))
                }
            }
        }
    }

    override suspend fun uploadImagesAlaCarte(
        projectId: RequestBody,
        params: RequestBody,
        ktpImage: MultipartBody.Part,
        faceImage: MultipartBody.Part,
        selfieMode: RequestBody
    ): KycResponse {
        Timber.d("uploadFaceImage=${convertRequestBodyToString(selfieMode)}")
        return uploadImages(projectId, params, ktpImage, faceImage, selfieMode)
    }

    private fun convertRequestBodyToString(requestBody: RequestBody): String {
        return Buffer().let {
            requestBody.writeTo(it)
            it.readUtf8()
        }
    }

    sealed class Case {
        object Success : Case()
        object NetworkFailed: Case()
        data class Retake(val code: ArrayList<Int>) : Case()
    }
}

