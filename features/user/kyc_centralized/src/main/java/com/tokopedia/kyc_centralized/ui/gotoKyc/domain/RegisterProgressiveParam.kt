package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class RegisterProgressiveParam (
    @SerializedName("projectID")
    val projectID: String = "",

    @SerializedName("challengeID")
    val challengeID: String = "",
): GqlParam
