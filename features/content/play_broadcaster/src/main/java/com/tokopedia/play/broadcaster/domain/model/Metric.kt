package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.socket.PlaySocketEnum
import com.tokopedia.play.broadcaster.socket.PlaySocketType


/**
 * Created by mzennis on 19/06/20.
 */
data class Metric(
        @SerializedName("new_participant")
        val newParticipant: MetricData = MetricData(),
        @SerializedName("pdp_visitor")
        val pdpVisitor: MetricData = MetricData(),
        @SerializedName("shop_visitor")
        val shopVisitor: MetricData = MetricData()
): PlaySocketType {

    override val type: PlaySocketEnum get() = PlaySocketEnum.Metric

    data class MetricData(
            @SerializedName("interval")
            val interval: Long = 0,
            @SerializedName("first_sentence")
            val firstSentence: String =  "",
            @SerializedName("second_sentence")
            val secondSentence: String = ""
    )
}
