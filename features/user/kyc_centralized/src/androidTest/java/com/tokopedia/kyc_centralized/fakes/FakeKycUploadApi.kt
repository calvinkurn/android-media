package com.tokopedia.kyc_centralized.fakes

import com.tokopedia.kyc_centralized.data.model.response.KycAppModel
import com.tokopedia.kyc_centralized.data.model.response.KycData
import com.tokopedia.kyc_centralized.data.model.response.KycResponse
import com.tokopedia.kyc_centralized.data.network.KycUploadApi
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.internal.http2.ErrorCode
import okhttp3.internal.http2.StreamResetException

class FakeKycUploadApi(private val case: Case = Case.Success) : KycUploadApi {

    var uploadCount = 0

    override suspend fun uploadImages(
        projectId: RequestBody,
        params: RequestBody,
        ktpImage: MultipartBody.Part,
        faceImage: MultipartBody.Part
    ): KycResponse {
        return when (case) {
            Case.Success -> KycResponse(
                data = KycData(
                    isSuccessRegister = true,
                )
            )
            Case.NetworkFailed -> {
                if (uploadCount == 0) {
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
                            listRetake = case.code,
                            app = KycAppModel(
                                title = "Verifikasi Wajah tidak berhasil",
                                subtitle = "di tempat terang, ya!",
                                button = "Verifikasi Ulang"
                            )
                        )
                    )
                } else {
                    KycResponse(
                        data = KycData(
                            isSuccessRegister = true,
                        )
                    )
                }
            }
        }
    }

    override suspend fun uploadImagesAlaCarte(
        projectId: RequestBody,
        params: RequestBody,
        ktpImage: MultipartBody.Part,
        faceImage: MultipartBody.Part
    ): KycResponse {
        TODO("Not yet implemented")
    }

    sealed class Case {
        object Success : Case()
        object NetworkFailed: Case()
        data class Retake(val code: ArrayList<Int>) : Case()
    }
}

