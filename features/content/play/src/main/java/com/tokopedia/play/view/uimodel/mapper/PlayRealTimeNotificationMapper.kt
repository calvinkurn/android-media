package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.realtimenotif.RealTimeNotification
import com.tokopedia.play.view.uimodel.RealTimeNotificationUiModel
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import javax.inject.Inject

/**
 * Created by jegul on 12/08/21
 */
class PlayRealTimeNotificationMapper @Inject constructor(
        private val htmlTextTransformer: HtmlTextTransformer
) {

    fun mapRealTimeNotification(response: RealTimeNotification) = RealTimeNotificationUiModel(
            icon = response.icon,
            text = htmlTextTransformer.transformWithStyle(response.copy),
            bgColor = response.backgroundColor,
    )
}