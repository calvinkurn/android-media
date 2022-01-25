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

    fun getSamplePlayWidget(): PlayWidgetUiModel = PlayWidgetUiModel(
        title = "Ini JumboTRON!",
        actionTitle = "Lihat semua",
        actionAppLink = "tokopedia://webview?titlebar=false\\u0026url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F",
        isActionVisible = true,
        config = getPlayWidgetConfigUiModel(),
        items = getSampleItemData(),
        background = getPlayWidgetBackgroundUiModel()
    )

    private fun getSampleItemData(): List<PlayWidgetItemUiModel> {
        val size = 5
        return List(size) {
            if (it == size-1) getSampleBannerModel()
            else {
                val channelType = when (cardItemTypeRandom.nextInt(0, 6)) {
                    0 -> PlayWidgetChannelType.Upcoming
                    1 -> PlayWidgetChannelType.Vod
                    2 -> PlayWidgetChannelType.FailedTranscoding
                    3 -> PlayWidgetChannelType.Deleting
                    4 -> PlayWidgetChannelType.Transcoding
                    else -> PlayWidgetChannelType.Live
                }
                getSampleChannelModel(channelType)
            }
        }
    }

    private fun getSampleBannerModel() = PlayWidgetBannerUiModel(
            imageUrl = "https://ecs7.tokopedia.net/img/cache/700/attachment/2020/9/2/inv/inv_b4cb80bc-aa3c-44b9-b368-ca373c488db1.jpg",
            appLink = "",
    )

    private fun getSampleChannelModel(channelType: PlayWidgetChannelType) = PlayWidgetChannelUiModel(
        channelId = "123",
        title = "Google Assistant review with me",
        channelType = channelType,
        appLink = "",
        startTime = "",
        totalView = PlayWidgetTotalView("10,0 rb", true),
        promoType = PlayWidgetPromoType.Default("Diskon 100%"),
        reminderType = PlayWidgetReminderType.Reminded,
        partner = PlayWidgetPartnerUiModel("123", "Google"),
        video = getVideoUiModel(channelType),
        hasAction = true,
        share = PlayWidgetShareUiModel(
            "TEST CHANNEL covert vod transcoding \nYuk, nonton siaran dari testtokoucup di Tokopedia PLAY! Bakal seru banget lho!\n https://tokopedia.link/hwql0mV2Wab",
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
            coverUrl = "https://cdn.jpegmini.com/user/images/slider_puffin_before_mobile.jpg",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
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