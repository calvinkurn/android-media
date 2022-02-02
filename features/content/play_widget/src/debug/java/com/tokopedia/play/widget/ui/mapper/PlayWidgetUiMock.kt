package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType
import kotlin.random.Random


/**
 * Created by mzennis on 06/10/20.
 */
object PlayWidgetUiMock {

    private val cardItemTypeRandom = Random(2)

    fun getSamplePlayWidget(
        title: String = "Video Menarik Untukmu!",
        isActionVisible: Boolean = true,
        items: List<PlayWidgetItemUiModel> = getSampleItemData()
    ): PlayWidgetUiModel = PlayWidgetUiModel(
        title = title,
        actionTitle = "Lihat Semua",
        actionAppLink = "tokopedia://webview?titlebar=false\\u0026url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F",
        isActionVisible = isActionVisible,
        config = getPlayWidgetConfigUiModel(),
        items = items,
        background = getPlayWidgetBackgroundUiModel()
    )

    private fun getSampleItemData(): List<PlayWidgetItemUiModel> {
        val size = 5
        return List(size) {
            if (it == size-3) getSampleBannerModel()
            else {
                val channelType = when (cardItemTypeRandom.nextInt(0, 6)) {
                    0 -> PlayWidgetChannelType.Upcoming
                    1 -> PlayWidgetChannelType.Vod
//                    2 -> PlayWidgetChannelType.FailedTranscoding
//                    3 -> PlayWidgetChannelType.Deleting
//                    4 -> PlayWidgetChannelType.Transcoding
                    else -> PlayWidgetChannelType.Live
                }
                getSampleChannelModel(channelType)
            }
        }
    }

    private fun getSampleBannerModel() = PlayWidgetBannerUiModel(
            imageUrl = "https://images.tokopedia.net/img/jJtrdn/2022/1/21/2f1ba9eb-a8d4-4de1-b445-ed66b96f26a9.jpg",
            appLink = "",
    )

    fun getSampleChannelModel(channelType: PlayWidgetChannelType) = PlayWidgetChannelUiModel(
        channelId = "123",
        title = "BARDI hingga Memory Mulai dari 1Rb! \uD83D\uDD25 \uD83D\uDE0D",
        channelType = channelType,
        appLink = "",
        startTime = "",
        totalView = PlayWidgetTotalView("33.1 rb", true),
        promoType = PlayWidgetPromoType.Default("Diskon 100%"),
        reminderType = PlayWidgetReminderType.NotReminded,
        partner = PlayWidgetPartnerUiModel("11232713", "Tokopedia Play"),
        video = getVideoUiModel(channelType),
        hasAction = true,
        share = PlayWidgetShareUiModel(
            "Udah pada nonton \\\"BARDI hingga Memory Mulai dari 1Rb! \uD83D\uDD25 \uD83D\uDE0D\\\" di Tokopedia Play? Ayo nonton bareng~ soalnya ini seru banget!\\nhttps://www.tokopedia.com/play/channel/272686?titlebar=false",
            isShow = true
        ),
        performanceSummaryLink = "tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fshop%2Fituajakak%2Fstatistic%2F10734",
        hasGiveaway = true,
        poolType = "",
        recommendationType = "",
        channelTypeTransition = PlayWidgetChannelTypeTransition(null, channelType),
    )

    private fun getVideoUiModel(channelType: PlayWidgetChannelType) = PlayWidgetVideoUiModel(
            id = "123",
            coverUrl = "https://images.tokopedia.net/img/jJtrdn/2022/1/21/2f1ba9eb-a8d4-4de1-b445-ed66b96f26a9.jpg?b=UaM%25G%23Rjn4WYVBx%5DjFWX%3D~t6bbWB0PkWkqoL",
            videoUrl = "https://vod.tokopedia.net/liveRecord/f85e64cd584687e170c3fb851569d466/play_record/7153b020-7e86-11ec-8a14-ce6d35e87b55/2022-01-26-17-00-45_2022-01-26-19-01-09.m3u8",
            isLive = channelType == PlayWidgetChannelType.Live
    )

    private fun getPlayWidgetConfigUiModel() = PlayWidgetConfigUiModel(
            autoRefresh = true,
            autoRefreshTimer = 30,
            autoPlay = true,
            autoPlayAmount = 3,
            maxAutoPlayCellularDuration = 5,
            maxAutoPlayWifiDuration = 10,
            businessWidgetPosition = 0
    )

    private fun getPlayWidgetBackgroundUiModel() = PlayWidgetBackgroundUiModel(
            overlayImageUrl = "https://ecs7.tokopedia.net/stessayalp/KV-Left-Widget-September.png",
            overlayImageAppLink = "tokopedia://webview?titlebar=false&url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F",
            overlayImageWebLink = "www.tokopedia.com/play/channels",
            gradientColors = listOf("#B4DAD1"),
            backgroundUrl = ""
    )
}