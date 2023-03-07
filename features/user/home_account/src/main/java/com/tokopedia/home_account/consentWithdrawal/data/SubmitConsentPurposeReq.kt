package com.tokopedia.home_account.consentWithdrawal.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SubmitConsentPurposeReq(
    @SerializedName("purposes")
    var param: PurposesParam = PurposesParam()
) : GqlParam

data class PurposesParam(
    @SerializedName("purposeID")
    var purposeID: String = "",
    @SerializedName("transactionType")
    var transactionType: String = "",
    @SerializedName("version")
    var version: String = "",
)
