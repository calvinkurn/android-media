package com.tokopedia.play.view.uimodel.recom.realtimenotif

import com.tokopedia.play.view.uimodel.RealTimeNotificationUiModel

/**
 * Created by jegul on 12/08/21
 */
data class PlayRealTimeNotificationConfig(
        val welcomeNotification: RealTimeNotificationUiModel = RealTimeNotificationUiModel(),
        val lifespan: Long = 0L,
)