package com.tokopedia.gopay.kyc.domain.data

import com.google.gson.annotations.SerializedName

data class InitiateKycGqlResponse(
    @SerializedName("walletappInitiateKYC")
    val initiateKycResponse: InitiateKycResponse
)

data class InitiateKycResponse(
    @SerializedName("code")
    val code: String,
    @SerializedName("data")
    val initiateKycData: InitiateKycData
)

data class InitiateKycData(
    @SerializedName("kyc_request_id")
    val kycRequestId: String,
    @SerializedName("docs")
    val kycDocuments: ArrayList<KycDocument>
)

data class KycDocument(
    @SerializedName("document_id")
    val documentId: String,
    @SerializedName("document_type")
    val documentType:String,
    @SerializedName("document_url")
    val documentUrl: String
)
