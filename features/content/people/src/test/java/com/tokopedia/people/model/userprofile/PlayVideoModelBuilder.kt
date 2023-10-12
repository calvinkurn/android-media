package com.tokopedia.people.model.userprofile

import com.tokopedia.people.views.uimodel.content.UserPlayVideoUiModel
import com.tokopedia.play.widget.ui.model.PartnerType
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelTypeTransition
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetPartnerUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetReminderType
import com.tokopedia.play.widget.ui.model.PlayWidgetShareUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetTotalView
import com.tokopedia.play.widget.ui.model.PlayWidgetVideoUiModel
import com.tokopedia.play.widget.ui.type.PlayWidgetChannelType
import com.tokopedia.play.widget.ui.type.PlayWidgetPromoType

/**
 * Created By : Jonathan Darwin on July 06, 2022
 */
class PlayVideoModelBuilder {

    fun buildModel(
        size: Int = 10,
        nextCursor: String = "",
        status: UserPlayVideoUiModel.Status = UserPlayVideoUiModel.Status.Success,
        channelType: PlayWidgetChannelType = PlayWidgetChannelType.Vod,
        reminderType: PlayWidgetReminderType = PlayWidgetReminderType.NotReminded
    ): UserPlayVideoUiModel {
        return UserPlayVideoUiModel(
            items = List(size) {
                PlayWidgetChannelUiModel(
                    channelId = it.toString(),
                    title = "BARDI hingga Memory Mulai dari 1Rb! \uD83D\uDD25 \uD83D\uDE0D",
                    channelType = channelType,
                    appLink = "",
                    startTime = "",
                    totalView = PlayWidgetTotalView("33.1 rb", true),
                    promoType = PlayWidgetPromoType.Default("Rilisan Spesial", true),
                    reminderType = reminderType,
                    partner = PlayWidgetPartnerUiModel(
                        "11232713",
                        "Tokopedia Play",
                        PartnerType.Shop,
                        "",
                        "",
                        ""
                    ),
                    video = PlayWidgetVideoUiModel(
                        id = "123",
                        coverUrl = "https://images.tokopedia.net/img/jJtrdn/2022/1/21/2f1ba9eb-a8d4-4de1-b445-ed66b96f26a9.jpg?b=UaM%25G%23Rjn4WYVBx%5DjFWX%3D~t6bbWB0PkWkqoL",
                        videoUrl = "https://vod.tokopedia.com/view/adaptive.m3u8?id=4d30328d17e948b4b1c4c34c5bb9f372",
                        isLive = false
                    ),
                    hasAction = true,
                    shouldShowPerformanceDashboard = false,
                    share = PlayWidgetShareUiModel(
                        "Udah pada nonton \\\"BARDI hingga Memory Mulai dari 1Rb! \uD83D\uDD25 \uD83D\uDE0D\\\" di Tokopedia Play? Ayo nonton bareng~ soalnya ini seru banget!\\nhttps://www.tokopedia.com/play/channel/272686?titlebar=false",
                        isShow = true
                    ),
                    performanceSummaryLink = "tokopedia://webview?url=https%3A%2F%2Fwww.tokopedia.com%2Fplay%2Fshop%2Fituajakak%2Fstatistic%2F10734",
                    hasGame = true,
                    poolType = "",
                    recommendationType = "",
                    channelTypeTransition = PlayWidgetChannelTypeTransition(null, channelType),
                    products = emptyList(),
                )
            },
            nextCursor = nextCursor,
            status = status
        )
    }
}
