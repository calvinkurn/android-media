package com.tokopedia.epharmacy.network.params

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetMiniConsultationBottomSheetParams(
    @SerializedName("data_type")
    var dataType: String = "",

    @SerializedName("enabler_name")
    var enablerName: String = ""
): GqlParam
