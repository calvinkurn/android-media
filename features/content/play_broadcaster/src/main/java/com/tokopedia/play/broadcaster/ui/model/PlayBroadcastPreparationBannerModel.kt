package com.tokopedia.play.broadcaster.ui.model

data class PlayBroadcastPreparationBannerModel(
    val type: String,
) {
    companion object {
        const val TYPE_SHORTS = "type_shorts"
        const val TYPE_DASHBOARD = "type_dashboard"
        const val TYPE_SHORTS_AFFILIATE = "type_shorts_affiliate"
    }
}
