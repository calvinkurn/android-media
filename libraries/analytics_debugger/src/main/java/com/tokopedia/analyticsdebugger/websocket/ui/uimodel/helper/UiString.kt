package com.tokopedia.analyticsdebugger.websocket.ui.uimodel.helper

import androidx.annotation.StringRes

/**
 * Created By : Jonathan Darwin on December 20, 2021
 */
sealed class UiString {
    data class Resource(@StringRes val idRes: Int): UiString()
    data class Text(val message: String): UiString()
}