package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SubmitChallengeParam (
    @SerializedName("param")
    val param: SubmitChallengeData = SubmitChallengeData()
): GqlParam

data class SubmitChallengeData (
    @SerializedName("challengeID")
    val challengeID: String = "",

    @SerializedName("answers")
    val answers: List<KycSubmitGoToChallengeAnswer> = listOf()
)

data class KycSubmitGoToChallengeAnswer (
    @SerializedName("questionId")
    val questionId: String = "",

    @SerializedName("answer")
    val answer: String = ""
): GqlParam
