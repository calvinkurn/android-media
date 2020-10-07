package com.tokopedia.play.widget.ui.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.ui.type.PlayWidgetCardItemType


/**
 * Created by mzennis on 05/10/20.
 */
data class PlayWidgetCardItemUiModel(
        val channelId: String,
        val title: String,
        val cardType: PlayWidgetCardItemType,
        val backgroundUrl: String,
        val appLink: String,
        val webLink: String,
        val startTime: String,
        val totalView: String,
        val totalViewVisible: Boolean,
        val hasPromo: Boolean,
        val activeReminder: Boolean,
        val isLive: Boolean,
        val partner: PlayWidgetPartnerUiModel,
        val video: PlayWidgetCardVideoUiModel
): ImpressHolder() {

    companion object {

        fun default() = PlayWidgetCardItemUiModel(
                channelId = "",
                title = "",
                cardType = PlayWidgetCardItemType.Unknown,
                backgroundUrl = "",
                appLink = "",
                webLink = "",
                startTime = "",
                totalView = "",
                totalViewVisible = false,
                hasPromo = false,
                activeReminder = false,
                isLive = false,
                partner = PlayWidgetPartnerUiModel("", ""),
                video = PlayWidgetCardVideoUiModel("", "", "")
        )
    }
}
