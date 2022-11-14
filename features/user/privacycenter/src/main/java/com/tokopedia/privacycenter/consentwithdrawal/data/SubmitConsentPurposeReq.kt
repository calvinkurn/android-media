package com.tokopedia.privacycenter.consentwithdrawal.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SubmitConsentPurposeReq(
    @SerializedName("purposes")
    val param: PurposesParam = PurposesParam()
) : GqlParam

data class PurposesParam(
    @SerializedName("purposeID")
    val purposeID: String = "",
    @SerializedName("transactionType")
    val transactionType: String = "",
    @SerializedName("version")
    val version: String = "",
)
