package com.tokopedia.play.view.event

import com.tokopedia.play.component.ComponentEvent
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.ui.pinned.model.PinnedMessage
import com.tokopedia.play.ui.toolbar.model.TitleToolbar
import com.tokopedia.play.view.type.PlayVODType
import com.tokopedia.play_common.state.TokopediaPlayVideoState

/**
 * Created by jegul on 02/12/19
 */
sealed class ScreenStateEvent : ComponentEvent {

    data class SetVideo(val vodType: PlayVODType) : ScreenStateEvent()
    data class SetTitle(val title: String): ScreenStateEvent()
    data class SetTitleToolbar(val titleToolbar: TitleToolbar): ScreenStateEvent()
    data class SetTotalViews(val totalView: String): ScreenStateEvent()
    data class SetTotalLikes(val totalLikes: String): ScreenStateEvent()
    data class SetPinned(val pinnedMessage: PinnedMessage) : ScreenStateEvent()
    data class SetQuickReply(val quickReply: List<String>) : ScreenStateEvent()
    data class IncomingChat(val chat: PlayChat) : ScreenStateEvent()
    data class VideoStateChanged(val state: TokopediaPlayVideoState) : ScreenStateEvent()
}