package com.tokopedia.play.view.uimodel.event

import androidx.annotation.StringRes
import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.play.view.uimodel.RealTimeNotificationUiModel
import com.tokopedia.play.view.uimodel.recom.PlayLikeBubbleConfig
import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * Created by jegul on 29/06/21
 */
sealed class PlayViewerNewUiEvent

data class ShowWinningDialogEvent(val userImageUrl: String, val dialogTitle: String, val dialogSubtitle: String) : PlayViewerNewUiEvent()

data class ShowCoachMarkWinnerEvent(val title: String, val subtitle: String) : PlayViewerNewUiEvent()
object HideCoachMarkWinnerEvent : PlayViewerNewUiEvent()

data class OpenPageEvent(val applink: String, val params: List<String> = emptyList(), val requestCode: Int? = null, val pipMode: Boolean = false) : PlayViewerNewUiEvent()

data class ShowInfoEvent(val message: UiString) : PlayViewerNewUiEvent()
data class ShowErrorEvent(val error: Throwable, val errMessage: UiString? = null) : PlayViewerNewUiEvent()

data class CopyToClipboardEvent(val content: String) : PlayViewerNewUiEvent()

/**
 * Real Time Notification
 */
data class ShowRealTimeNotificationEvent(
        val notification: RealTimeNotificationUiModel,
) : PlayViewerNewUiEvent()

/**
 * Multiple Likes
 */
data class AnimateLikeEvent(val fromIsLiked: Boolean) : PlayViewerNewUiEvent()
object RemindToLikeEvent : PlayViewerNewUiEvent()
sealed class ShowLikeBubbleEvent : PlayViewerNewUiEvent() {

    abstract val count: Int
    abstract val reduceOpacity: Boolean

    data class Single(
        override val count: Int,
        override val reduceOpacity: Boolean,
        val config: PlayLikeBubbleConfig,
    ) : ShowLikeBubbleEvent()

    data class Burst(
        override val count: Int,
        override val reduceOpacity: Boolean,
        val config: PlayLikeBubbleConfig,
    ) : ShowLikeBubbleEvent()
}
data class PreloadLikeBubbleIconEvent(val urls: Set<String>) : PlayViewerNewUiEvent()

/**
 * Sharing Experience
 */
data class OpenSharingOptionEvent(val title: String, val coverUrl: String) : PlayViewerNewUiEvent()
data class OpenSelectedSharingOptionEvent(val title: String, val description: String, val url: String): PlayViewerNewUiEvent()
data class OpenShareExperienceEvent(val shareModel: ShareModel, val linkerShareResult: LinkerShareResult?, val shareString: String): PlayViewerNewUiEvent()

//---------------------

sealed class UiString {

    data class Resource(@StringRes val resource: Int) : UiString()
    data class Text(val text: String) : UiString()
}

data class AllowedWhenInactiveEvent(
    val event: PlayViewerNewUiEvent
) : PlayViewerNewUiEvent() {

    init {
        require(event !is AllowedWhenInactiveEvent)
    }
}