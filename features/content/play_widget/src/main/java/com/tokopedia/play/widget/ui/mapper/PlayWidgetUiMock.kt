package com.tokopedia.play.widget.ui.mapper

import com.tokopedia.play.widget.ui.model.*
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import kotlin.random.Random


/**
 * Created by mzennis on 06/10/20.
 */
object PlayWidgetUiMock {

    private val cardItemTypeRandom = Random(2)

    fun getPlayWidgetMedium(): PlayWidgetUiModel = getSamplePlayMediumWidget()

    fun getPlayWidgetSmall(): PlayWidgetUiModel = getSamplePlaySmallWidget()

    private fun getSamplePlaySmallWidget(): PlayWidgetUiModel = PlayWidgetUiModel.Small(
            title = "Yuk Nonton Sekarang!",
            actionTitle = "Lihat semua",
            actionAppLink = "tokopedia://webview?titlebar=false\\u0026url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F",
            isActionVisible = true,
            config = getPlayWidgetConfigUiModel(),
            items = getSampleSmallCardData(),
            useHeader = true
    )

    private fun getSamplePlayMediumWidget(): PlayWidgetUiModel = PlayWidgetUiModel.Medium(
            title = "Yuk Nonton Sekarang!",
            actionTitle = "Lihat semua",
            actionAppLink = "tokopedia://webview?titlebar=false\\u0026url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fchannels%2F",
            isActionVisible = true,
            background = getPlayWidgetBackgroundUiModel(),
            config = getPlayWidgetConfigUiModel(),
            items = getSampleMediumCardData()
    )

    private fun getSampleSmallCardData(): List<PlayWidgetSmallItemUiModel> {
        return List(5) {
            if (it == 0) getSampleSmallCardBanner()
            else {
                val channelType = when (cardItemTypeRandom.nextInt(0, 4)) {
                    0 -> PlayWidgetChannelType.Upcoming
                    1 -> PlayWidgetChannelType.Vod
                    else -> PlayWidgetChannelType.Live
                }
                getSampleSmallChannelCardBanner(channelType)
            }
        }
    }

    private fun getSampleMediumCardData(): List<PlayWidgetMediumItemUiModel> {
        return List(5) {
            when (it) {
                0 -> getSampleMediumCardOverlayBanner()
                4 -> getSampleMediumCardBanner()
                else -> {
                    val channelType = when (cardItemTypeRandom.nextInt(0, 4)) {
                        0 -> PlayWidgetChannelType.Upcoming
                        1 -> PlayWidgetChannelType.Vod
                        else -> PlayWidgetChannelType.Live
                    }
                    getSampleMediumChannelCardBanner(channelType)
                }
            }
        }
    }

    private fun getSampleSmallCardBanner() = PlayWidgetSmallBannerUiModel(
            imageUrl = "https://cdn.jpegmini.com/user/images/slider_puffin_before_mobile.jpg",
            appLink = "",
            webLink = ""
    )

    private fun getSampleSmallChannelCardBanner(channelType: PlayWidgetChannelType) = PlayWidgetSmallChannelUiModel(
            channelId = "123",
            title = "Google Assistant review with me",
            channelType = channelType,
            appLink = "",
            webLink = "",
            startTime = "",
            totalView = "10,0 rb",
            totalViewVisible = true,
            hasPromo = cardItemTypeRandom.nextBoolean(),
            video = getVideoUiModel(channelType)
    )

    private fun getSampleMediumCardOverlayBanner() = PlayWidgetMediumOverlayUiModel(
            imageUrl = "https://ecs7.tokopedia.net/stessayalp/KV-Left-Widget-September.png",
            appLink = "",
            webLink = ""
    )

    private fun getSampleMediumCardBanner() = PlayWidgetMediumBannerUiModel(
            imageUrl = "https://ecs7.tokopedia.net/img/cache/700/attachment/2020/9/2/inv/inv_b4cb80bc-aa3c-44b9-b368-ca373c488db1.jpg",
            appLink = "",
            webLink = "",
            partner = PlayWidgetPartnerUiModel("123", "Google")
    )

    private fun getSampleMediumChannelCardBanner(channelType: PlayWidgetChannelType) = PlayWidgetMediumChannelUiModel(
            channelId = "123",
            title = "Google Assistant review with me",
            channelType = channelType,
            appLink = "",
            webLink = "",
            startTime = "",
            totalView = "10,0 rb",
            totalViewVisible = true,
            hasPromo = cardItemTypeRandom.nextBoolean(),
            reminderType = PlayWidgetReminderType.UnRemind,
            partner = PlayWidgetPartnerUiModel("123", "Google"),
            video = getVideoUiModel(channelType),
            hasAction = true,
            channelTypeTransition = PlayWidgetChannelTypeTransition(null, channelType),
            share = PlayWidgetShareUiModel(
                    "TEST CHANNEL covert vod transcoding \nYuk, nonton siaran dari testtokoucup di Tokopedia PLAY! Bakal seru banget lho!\n https://tokopedia.link/hwql0mV2Wab",
                    isShow = true
            ),
            performanceSummaryLink = "tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fshop%2Fituajakak%2Fstatistic%2F10734"
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
            gradientColors = listOf(),
            backgroundUrl = ""
    )
}