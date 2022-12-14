package com.tokopedia.tokopedianow.data

import com.tokopedia.play.widget.ui.model.PlayWidgetBackgroundUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelTypeTransition
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetConfigUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetShareUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalView
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType

fun createPlayWidgetChannel(channelId: String, totalView: String): PlayWidgetChannelUiModel {
    return PlayWidgetChannelUiModel(
        channelId = channelId,
        title = "BARDI hingga Memory Mulai dari 1Rb! \uD83D\uDD25 \uD83D\uDE0D",
        channelType = PlayWidgetChannelType.Live,
        appLink = "",
        startTime = "",
        totalView = PlayWidgetTotalView(totalView, true),
        promoType = PlayWidgetPromoType.Default("Rilisan Spesial", true),
        reminderType = PlayWidgetReminderType.NotReminded,
        partner = PlayWidgetPartnerUiModel("11232713", "Tokopedia Play"),
        video = createVideoUiModel(PlayWidgetChannelType.Live),
        hasAction = true,
        share = PlayWidgetShareUiModel(
            "Udah pada nonton \\\"BARDI hingga Memory Mulai dari 1Rb! \uD83D\uDD25 \uD83D\uDE0D\\\" di Tokopedia Play? Ayo nonton bareng~ soalnya ini seru banget!\\nhttps://www.tokopedia.com/play/channel/272686?titlebar=false",
            isShow = true
        ),
        performanceSummaryLink = "tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fshop%2Fituajakak%2Fstatistic%2F10734",
        hasGame = true,
        poolType = "",
        recommendationType = "",
        channelTypeTransition = PlayWidgetChannelTypeTransition(null, PlayWidgetChannelType.Live),
    )
}

fun createPlayWidgetUiModel(
    title: String = "Video Menarik Untukmu!",
    appLink: String = "tokopedia://webview?titlebar=false\\u0026url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F",
    isActionVisible: Boolean = true,
    items: List<PlayWidgetItemUiModel> = emptyList()
): PlayWidgetUiModel = PlayWidgetUiModel(
    title = title,
    actionTitle = "Lihat Semua",
    actionAppLink = appLink,
    isActionVisible = isActionVisible,
    config = createPlayWidgetConfigUiModel(),
    items = items,
    background = createPlayWidgetBackgroundUiModel()
)

fun createVideoUiModel(channelType: PlayWidgetChannelType) = PlayWidgetVideoUiModel(
    id = "123",
    coverUrl = "https://images.tokopedia.net/img/jJtrdn/2022/1/21/2f1ba9eb-a8d4-4de1-b445-ed66b96f26a9.jpg?b=UaM%25G%23Rjn4WYVBx%5DjFWX%3D~t6bbWB0PkWkqoL",
    videoUrl = "https://vod.tokopedia.net/liveRecord/f85e64cd584687e170c3fb851569d466/play_record/7153b020-7e86-11ec-8a14-ce6d35e87b55/2022-01-26-17-00-45_2022-01-26-19-01-09.m3u8",
    isLive = channelType == PlayWidgetChannelType.Live
)

fun createPlayWidgetConfigUiModel() = PlayWidgetConfigUiModel(
    autoRefresh = true,
    autoRefreshTimer = 30,
    autoPlay = true,
    autoPlayAmount = 3,
    maxAutoPlayCellularDuration = 5,
    maxAutoPlayWifiDuration = 10,
    businessWidgetPosition = 0
)

fun createPlayWidgetBackgroundUiModel() = PlayWidgetBackgroundUiModel(
    overlayImageUrl = "https://images.tokopedia.net/stessayalp/KV-Left-Widget-September.png",
    overlayImageAppLink = "tokopedia://webview?titlebar=false&url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F",
    overlayImageWebLink = "www.tokopedia.com/play/channels",
    gradientColors = listOf("#B4DAD1"),
    backgroundUrl = ""
)
