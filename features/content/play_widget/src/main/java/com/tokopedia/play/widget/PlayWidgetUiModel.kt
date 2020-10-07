package com.tokopedia.play.widget

import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetMediumChannelType


/**
 * Created by mzennis on 05/10/20.
 */
sealed class PlayWidgetUiModel

sealed class PlayWidgetItemUiModel

data class PlayWidgetMediumUiModel(
        val title: String,
        val actionTitle: String,
        val actionAppLink: String,
        val actionWebLink: String,
        val background: PlayWidgetBackgroundUiModel,
        val config: PlayWidgetConfigUiModel,
        val items: List<PlayWidgetItemUiModel>
): PlayWidgetUiModel()

object PlayWidgetMediumOverlayUiModel: PlayWidgetItemUiModel()

data class PlayWidgetMediumBannerUiModel(
        val imageUrl: String,
        val appLink: String,
        val webLink: String
): PlayWidgetItemUiModel()

data class PlayWidgetMediumChannelUiModel(
        val channelId: String,
        val title: String,
        val appLink: String,
        val webLink: String,
        val startTime: String,
        val totalView: String,
        val totalViewVisible: Boolean,
        val hasPromo: Boolean,
        val activeReminder: Boolean,
        val partner: PlayWidgetPartnerUiModel,
        val video: PlayWidgetVideoUiModel,
        val channelType: PlayWidgetMediumChannelType
): PlayWidgetItemUiModel()