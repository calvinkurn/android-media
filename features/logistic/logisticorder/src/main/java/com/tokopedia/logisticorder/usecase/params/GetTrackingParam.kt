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
        val orderId: String = "",

        @SerializedName("order_tx_id")
        val orderTxId: String = "",

        @SerializedName("group_type")
        val groupType: Int = 0,

        @SerializedName("from")
        val from: String = ""
    ) : GqlParam
}
