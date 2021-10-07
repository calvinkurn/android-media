package com.tokopedia.play.view.uimodel.mapper

import com.tokopedia.play.data.realtimenotif.RealTimeNotification
import com.tokopedia.play.di.PlayScope
import com.tokopedia.play.view.uimodel.RealTimeNotificationUiModel
import com.tokopedia.play_common.transformer.HtmlTextTransformer
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by jegul on 12/08/21
 */
@PlayScope
class PlayRealTimeNotificationMapper @Inject constructor(
        private val userSession: UserSessionInterface,
        private val htmlTextTransformer: HtmlTextTransformer,
) {

    fun mapRealTimeNotification(response: RealTimeNotification) = RealTimeNotificationUiModel(
            icon = response.icon,
            text = htmlTextTransformer.transformWithStyle(response.copy),
            bgColor = response.backgroundColor,
    )

    fun mapWelcomeFormat(response: RealTimeNotification) = mapRealTimeNotification(
            response.copy(
                    copy = response.copy.replace(FORMAT_NAME, userSession.name)
            )
    )

    companion object {
        private const val FORMAT_NAME = "{{name}}"
    }
}