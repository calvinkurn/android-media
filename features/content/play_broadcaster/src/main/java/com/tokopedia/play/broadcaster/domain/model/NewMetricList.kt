package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.socket.PlaySocketEnum
import com.tokopedia.play.broadcaster.socket.PlaySocketType

/**
 * Created by jegul on 21/09/20
 */

/*
 {
    "interval": 1800, //miliseconds
    "sentence": "<b>2</b> penonton <br> bergabung",
    "type": "new_participant",
    "icon": "https://www.tokopedia.com/icon.png"
}
 */

data class NewMetricList(
        val metricList: List<NewMetric>
) : PlaySocketType {

    data class NewMetric(
            @SerializedName("interval")
            val interval: Long = 1000L,
            @SerializedName("sentence")
            val sentence: String = "",
            @SerializedName("type")
            val metricType: String = "",
            @SerializedName("icon")
            val icon: String = ""
    )

    override val type: PlaySocketEnum
        get() = PlaySocketEnum.NewMetric
}

