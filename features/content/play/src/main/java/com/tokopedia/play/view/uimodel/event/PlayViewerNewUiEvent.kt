package com.tokopedia.play.view.uimodel.event

import androidx.annotation.StringRes
import com.tokopedia.play.view.uimodel.RealTimeNotificationUiModel

/**
 * Created by jegul on 29/06/21
 */
interface AllowedWhenInactiveEvent

sealed class PlayViewerNewUiEvent

data class ShowWinningDialogEvent(val userImageUrl: String, val dialogTitle: String, val dialogSubtitle: String) : PlayViewerNewUiEvent()
data class ShowCoachMarkWinnerEvent(val title: String, val subtitle: String) : PlayViewerNewUiEvent()
data class OpenPageEvent(val applink: String, val params: List<String> = emptyList(), val requestCode: Int? = null, val pipMode: Boolean = false) : PlayViewerNewUiEvent()
sealed class ShowToasterEvent : PlayViewerNewUiEvent() {
    abstract val message: UiString

    data class Info(override val message: UiString) : ShowToasterEvent()
    data class Error(override val message: UiString) : ShowToasterEvent()
}
object HideCoachMarkWinnerEvent : PlayViewerNewUiEvent()
data class CopyToClipboardEvent(val content: String) : PlayViewerNewUiEvent()

/**
 * Real Time Notification
 */
data class ShowRealTimeNotificationEvent(
        val notification: RealTimeNotificationUiModel,
) : PlayViewerNewUiEvent()

//---------------------

sealed class UiString {

    data class Resource(@StringRes val resource: Int) : UiString()
    data class Text(val text: String) : UiString()
}

private const val REAL_TIME_NOTIF_ANIMATION_DURATION_IN_MS = 500L