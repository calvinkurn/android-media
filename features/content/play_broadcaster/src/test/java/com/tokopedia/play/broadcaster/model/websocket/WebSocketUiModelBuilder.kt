package com.tokopedia.play.broadcaster.model.websocket

import com.tokopedia.play.broadcaster.ui.model.PlayMetricUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalLikeUiModel
import com.tokopedia.play.broadcaster.ui.model.TotalViewUiModel

/**
 * Created By : Jonathan Darwin on February 21, 2022
 */
class WebSocketUiModelBuilder {

    /** New Metrics */
    fun buildNewMetricString(
        size: Int = 1,
        interval: Long = 1000,
        sentence: String = "<b>2</b> penonton <br> bergabung",
        type: String = "",
        icon: String = "",
    ): String {
        var data = ""
        List(size) {
            data += """
                {
                    "interval": $interval,
                    "sentence": "$sentence",
                    "type": "$type",
                    "icon": "$icon"
                }
            """.trimIndent()
            if(it != size-1) data += ","
        }

        return """
            {
                "type": "GENERAL_BULK_EVENT_NOTIF",
                "data": [
                    $data
                ]
            }
        """.trimIndent()
    }

    fun buildNewMetricModelList(
        size: Int = 1,
        iconUrl: String =  "",
        spannedSentence: String = "<b>2</b> penonton <br> bergabung",
        type: String = "",
        interval: Long = 1000L,
    ) = List(size) {
        PlayMetricUiModel(
            iconUrl = iconUrl,
            spannedSentence = spannedSentence,
            type = type,
            interval = interval,
        )
    }

    /** TOTAL_VIEW */
    fun buildTotalViewString(
        channelId: Int = 1,
        totalView: Long = 1,
        totalViewFmt: String = "1",
    ) = """
        {
            "type": "TOTAL_VIEW",
            "data": {
                "channel_id" : $channelId,
                "total_view" : $totalView,
                "total_view_formatted" : "$totalViewFmt"
            }
        }
    """.trimIndent()

    fun buildTotalViewModel(
        totalViewFmt: String = "1",
    ) = TotalViewUiModel(
        totalView = totalViewFmt,
    )

    /** TOTAL_LIKE */
    fun buildTotalLikeString(
        channelId: Int = 1,
        totalLike: Long = 1,
        totalLikeFmt: String = "1",
    ) = """
        {
            "type": "TOTAL_LIKE",
            "data": {
                "channel_id" : $channelId,
                "total_like" : $totalLike,
                "total_like_formatted" : "$totalLikeFmt"
            }
        }
    """.trimIndent()

    fun buildTotalLikeModel(
        totalLikeFmt: String = "1",
    ) = TotalLikeUiModel(
        totalLike = totalLikeFmt,
    )
}