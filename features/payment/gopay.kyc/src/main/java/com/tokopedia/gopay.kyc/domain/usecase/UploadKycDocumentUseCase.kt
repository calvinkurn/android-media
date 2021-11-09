package com.tokopedia.gopay.kyc.domain.usecase

import com.tokopedia.common.network.data.source.cloud.api.RestApi
import com.tokopedia.common.network.util.RestConstant
import com.tokopedia.gopay.kyc.domain.data.KycDocument
import com.tokopedia.gopay.kyc.viewmodel.GoPayKycImageUploadViewModel
import com.tokopedia.usecase.coroutines.UseCase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject


class UploadKycDocumentUseCase @Inject constructor(private val restApi: RestApi) : UseCase<Boolean>() {

    private var ktpDocumentUrl = ""
    private var selfieDocumentUrl = ""
    private var ktpPath = ""
    private var selfieKtpPath = ""

    fun setRequestParams(
        documentList: ArrayList<KycDocument>,
        ktpPath: String,
        selfieKtpPath: String
    ) {
        this.ktpPath = ktpPath
        this.selfieKtpPath = selfieKtpPath
        for (document in documentList) {
            if (document.documentType == GoPayKycImageUploadViewModel.DOCUMENT_TYPE_KYC_PROOF) {
                this.ktpDocumentUrl = document.documentUrl
            } else this.selfieDocumentUrl = document.documentUrl
        }
    }

    fun uploadKycDocuments(onSuccess: (Boolean) -> Unit, onError: (Throwable) -> Unit) {
        this.execute({
            onSuccess(it)
        }, {
            onError(it)
        })
    }

    override suspend fun executeOnBackground(): Boolean {
        val ktpFile = File(ktpPath)
        val selfieKtpFile = File(selfieKtpPath)
        val ktpUploadResponse = restApi.putRequestBodyDeferred(
            ktpDocumentUrl,
            ktpFile.asRequestBody(MEDIA_TYPE_IMAGE.toMediaTypeOrNull()),
            mapOf()
        )
        val selfieKtpUploadResponse = restApi.putRequestBodyDeferred(
            selfieDocumentUrl,
            selfieKtpFile.asRequestBody(MEDIA_TYPE_IMAGE.toMediaTypeOrNull()),
            mapOf()
        )
        return (RestConstant.HTTP_SUCCESS == ktpUploadResponse.code() && RestConstant.HTTP_SUCCESS == selfieKtpUploadResponse.code())
    }

    companion object {
        private const val MEDIA_TYPE_IMAGE = "image/*"
    }


}
