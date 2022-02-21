package com.tokopedia.play.view.uimodel.event

import com.tokopedia.linker.model.LinkerShareResult
import com.tokopedia.universal_sharing.view.model.ShareModel

/**
 * Created By : Jonathan Darwin on November 17, 2021
 */
sealed class PlayUpcomingUiEvent {
    /**
     * General
     */
    data class OpenPageEvent(val applink: String, val params: List<String> = emptyList(), val requestCode: Int? = null) : PlayUpcomingUiEvent()
    data class ShowInfoEvent(val message: UiString) : PlayUpcomingUiEvent()
    data class ShowInfoWithActionEvent(val message: UiString, val action: () -> Unit) : PlayUpcomingUiEvent()

    /**
     * Upcoming
     */
    data class RemindMeEvent(val message: UiString, val isSuccess: Boolean): PlayUpcomingUiEvent()
    object RefreshChannelEvent: PlayUpcomingUiEvent()

    /**
     * Share Experience
     */
    data class SaveTemporarySharingImage(val imageUrl: String): PlayUpcomingUiEvent()
    data class OpenSharingOptionEvent(val title: String, val coverUrl: String, val userId: String, val channelId: String) : PlayUpcomingUiEvent()
    data class OpenSelectedSharingOptionEvent(val linkerShareResult: LinkerShareResult?, val shareModel: ShareModel, val shareString: String): PlayUpcomingUiEvent()
    object CloseShareExperienceBottomSheet: PlayUpcomingUiEvent()
    object ErrorGenerateShareLink: PlayUpcomingUiEvent()

    /**
     * Other
     */
    data class CopyToClipboardEvent(val content: String) : PlayUpcomingUiEvent()
}