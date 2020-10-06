package com.tokopedia.play.widget.ui.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.ui.type.PlayWidgetCardType


/**
 * Created by mzennis on 05/10/20.
 */
data class PlayWidgetCardUiModel(
        val channelId: String,
        val title: String,
        val widgetType: PlayWidgetCardType,
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
): ImpressHolder()
