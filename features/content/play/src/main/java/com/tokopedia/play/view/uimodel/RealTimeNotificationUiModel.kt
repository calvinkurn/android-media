package com.tokopedia.play.view.uimodel

import android.text.Spanned
import android.text.SpannedString

/**
 * Created by jegul on 03/08/21
 */
data class RealTimeNotificationUiModel(
        val icon: String = "",
        val text: Spanned = SpannedString(""),
        val bgColor: String = "",
)