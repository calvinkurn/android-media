package com.tokopedia.play.model

import android.text.SpannedString
import com.tokopedia.play.view.uimodel.RealTimeNotificationUiModel

/**
 * Created by jegul on 23/08/21
 */
class PlayRtnModelBuilder {

    fun buildNotification(
            icon: String = "",
            text: SpannedString = SpannedString("halo"),
            bgColor: String = "",
    ) = RealTimeNotificationUiModel(
            icon = icon,
            text = text,
            bgColor = bgColor,
    )
}