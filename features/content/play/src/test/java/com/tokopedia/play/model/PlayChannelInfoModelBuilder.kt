package com.tokopedia.play.model

import com.tokopedia.play.view.type.PlayChannelType
import com.tokopedia.play.view.uimodel.RealTimeNotificationUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelDetailUiModel
import com.tokopedia.play.view.uimodel.recom.PlayChannelInfoUiModel
import com.tokopedia.play.view.uimodel.recom.PlayShareInfoUiModel
import com.tokopedia.play.view.uimodel.recom.realtimenotif.PlayRealTimeNotificationConfig

/**
 * Created by jegul on 09/02/21
 */
class PlayChannelInfoModelBuilder {

    fun buildChannelDetail(
        shareInfo: PlayShareInfoUiModel = buildShareInfo(),
        channelInfo: PlayChannelInfoUiModel = buildChannelInfo(),
        rtnConfigInfo: PlayRealTimeNotificationConfig = buildRtnConfigInfo()
    ) = PlayChannelDetailUiModel(
            shareInfo = shareInfo,
            channelInfo = channelInfo,
            rtnConfigInfo = rtnConfigInfo,
    )

    fun buildShareInfo(
            content: String = "Ayo nonton sekarang",
            shouldShow: Boolean = true
    ) = PlayShareInfoUiModel(
            content = content,
            shouldShow = shouldShow
    )

    fun buildChannelInfo(
            id: String = "1",
            channelType: PlayChannelType = PlayChannelType.Live,
            backgroundUrl: String = "https://www.tokopedia.com",
            title: String = ""
    ) = PlayChannelInfoUiModel(
            id = id,
            channelType = channelType,
            backgroundUrl = backgroundUrl,
            title = title
    )

    fun buildRtnConfigInfo(
            welcomeNotification: RealTimeNotificationUiModel = PlayRtnModelBuilder().buildNotification(),
            lifespan: Long = 0L,
    ) = PlayRealTimeNotificationConfig(
            welcomeNotification = welcomeNotification,
            lifespan = lifespan,
    )
}