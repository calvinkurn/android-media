package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName


/**
 * Created by mzennis on 19/06/20.
 */
data class Metric(
        @SerializedName("new_participant")
        val newParticipant: MetricData,
        @SerializedName("pdp_visitor")
        val pdpVisitor: MetricData,
        @SerializedName("shop_visitor")
        val shopVisitor: MetricData
) {

    data class MetricData(
            @SerializedName("interval")
            val interval: Long,
            @SerializedName("first_sentence")
            val firstSentence: String,
            @SerializedName("second_sentence")
            val secondSentence: String
    )

    val socketType = "BULK_EVENT_NOTIF"
}
