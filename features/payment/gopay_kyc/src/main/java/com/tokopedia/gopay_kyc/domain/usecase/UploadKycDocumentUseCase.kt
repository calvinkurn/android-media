package com.tokopedia.gopay_kyc.domain.usecase

import android.util.Log
import com.tokopedia.common.network.data.source.cloud.api.RestApi
import com.tokopedia.common.network.util.RestConstant
import com.tokopedia.gopay_kyc.domain.data.KycDocument
import com.tokopedia.gopay_kyc.viewmodel.GoPayKycImageUploadViewModel
import com.tokopedia.usecase.coroutines.UseCase
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


class UploadKycDocumentUseCase @Inject constructor(private val restApi: RestApi) : UseCase<Boolean>() {

    private var ktpDocumentUrl = ""
    private var selfieDocumentUrl = ""
    private var ktpPath = ""
    private var selfieKtpPath = ""

    private val MEDIA_TYPE_IMAGE = "image/*"

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
            RequestBody.create(MediaType.parse(MEDIA_TYPE_IMAGE), ktpFile),
            mapOf()
        )
        val selfieKtpUploadResponse = restApi.putRequestBodyDeferred(
            selfieDocumentUrl,
            RequestBody.create(MediaType.parse(MEDIA_TYPE_IMAGE), selfieKtpFile),
            mapOf()
        )
        return (RestConstant.HTTP_SUCCESS == ktpUploadResponse.code() && RestConstant.HTTP_SUCCESS == selfieKtpUploadResponse.code())
    }


}
