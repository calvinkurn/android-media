package com.tokopedia.logisticorder.usecase.params

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

/**
 * Created by irpan on 04/04/23.
 */
data class GetTrackingParam(
    @SerializedName("input")
    val trackingParam: TrackingParam = TrackingParam()
) : GqlParam {
    data class TrackingParam(
        @SerializedName("order_id")
        val shopId: String = "",
        @SerializedName("from")
        val from: String = ""
    ) : GqlParam
}
