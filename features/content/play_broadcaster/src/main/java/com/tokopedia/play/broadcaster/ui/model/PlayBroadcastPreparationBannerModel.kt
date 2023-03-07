package com.tokopedia.play.broadcaster.ui.model

import android.content.Context
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.play.broadcaster.R

data class PlayBroadcastPreparationBannerModel(
    val title: String,
    val description: String,
    val icon: Int,
    val type: String,
) {
    companion object {
        const val TYPE_SHORTS = "type_shorts"
        const val TYPE_DASHBOARD = "type_dashboard"

        fun bannerShortsEntryPoint(context: Context) = PlayBroadcastPreparationBannerModel(
            title = context.getString(R.string.play_bro_banner_shorts_title),
            description = context.getString(R.string.play_bro_banner_shorts_description),
            icon = IconUnify.SHORT_VIDEO,
            type = TYPE_SHORTS,
        )

        fun bannerPerformanceEntryPoint(context: Context) = PlayBroadcastPreparationBannerModel(
            title = context.getString(R.string.play_bro_banner_performance_title),
            description = context.getString(R.string.play_bro_banner_performance_subtitle),
            icon = IconUnify.GRAPH_REPORT,
            type = TYPE_DASHBOARD,
        )
    }
}
