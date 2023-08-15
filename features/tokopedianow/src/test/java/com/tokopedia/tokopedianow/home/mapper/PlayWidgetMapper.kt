package com.tokopedia.tokopedianow.home.mapper

import com.tokopedia.play.widget.ui.PlayWidgetState
import com.tokopedia.play.widget.ui.model.PartnerType
import com.tokopedia.play.widget.ui.model.PlayGridType
import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelTypeTransition
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetShareUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalView
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType

object PlayWidgetMapper {

    fun createPlayWidgetState(): PlayWidgetState {
        return PlayWidgetState(
            model = PlayWidgetUiModel(
                title = "",
                actionTitle = "",
                actionAppLink = "",
                isActionVisible = false,
                config = PlayWidgetConfigUiModel.Empty,
                background = PlayWidgetBackgroundUiModel.Empty,
                items = listOf(
                    PlayWidgetChannelUiModel(
                        "1",
                        "",
                        "",
                        "",
                        PlayWidgetTotalView("", false),
                        PlayWidgetPromoType.Default("", false),
                        PlayWidgetReminderType.NotReminded,
                        PlayWidgetPartnerUiModel("", "", PartnerType.Unknown, "", "", ""),
                        PlayWidgetVideoUiModel("", false, "", ""),
                        PlayWidgetChannelType.Upcoming,
                        false,
                        PlayWidgetShareUiModel("", false),
                        "",
                        "",
                        "",
                        false,
                        channelTypeTransition = PlayWidgetChannelTypeTransition(null, PlayWidgetChannelType.Upcoming),
                        shouldShowPerformanceDashboard = false,
                        products = emptyList(),
                        gridType = PlayGridType.Unknown,
                        extras = emptyMap(),
                    )
                )
            )
        )
    }
}
