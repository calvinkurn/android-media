package com.tokopedia.kyc_centralized.ui.gotoKyc.domain

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SubmitChallengeParam (
    @SerializedName("challengeID")
    val challengeID: String = "",

    @SerializedName("answers")
    val answers: List<KycSubmitGoToChallengeAnswer> = listOf()
): GqlParam

data class KycSubmitGoToChallengeAnswer (
    @SerializedName("questionId")
    val questionId: String = "",

    @SerializedName("answer")
    val answer: String = ""
): GqlParam
