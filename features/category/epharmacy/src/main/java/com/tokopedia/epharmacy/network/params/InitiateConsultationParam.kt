package com.tokopedia.epharmacy.network.params

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class InitiateConsultationParam(
    @SerializedName("input")
    var input: InitiateConsultationParamInput
) : GqlParam {
    data class InitiateConsultationParamInput(
        @SerializedName("epharmacy_group_id")
        var epharmacyGroupId: String = "",
        @SerializedName("source")
        var source: String = ""
    )
}
