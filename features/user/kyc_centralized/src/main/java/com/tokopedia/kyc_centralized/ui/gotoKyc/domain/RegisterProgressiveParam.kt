package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class RegisterProgressiveParam (
    @SerializedName("param")
    val param: RegisterProgressiveData = RegisterProgressiveData()
): GqlParam

data class RegisterProgressiveData (
    @SerializedName("projectID")
    val projectID: Int = 0,

    @SerializedName("challengeID")
    val challengeID: String = ""
)
