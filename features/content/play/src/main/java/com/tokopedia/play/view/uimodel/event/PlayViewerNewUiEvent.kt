package com.tokopedia.play.view.uimodel.event

import androidx.annotation.StringRes

/**
 * Created by jegul on 29/06/21
 */
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


//---------------------

sealed class UiString {

    data class Resource(@StringRes val resource: Int) : UiString()
    data class Text(val text: String) : UiString()
}