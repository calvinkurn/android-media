package com.tokopedia.play.view.event

import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.play.component.ComponentEvent
import com.tokopedia.play.ui.chatlist.model.PlayChat
import com.tokopedia.play.view.uimodel.*

/**
 * Created by jegul on 02/12/19
 */
sealed class ScreenStateEvent : ComponentEvent {

    data class SetVideo(val videoPlayer: ExoPlayer) : ScreenStateEvent()
    data class SetChannelTitle(val title: String): ScreenStateEvent()
    data class SetPartnerInfo(val partnerInfo: PartnerInfoUiModel): ScreenStateEvent()
    data class SetTotalViews(val totalView: TotalViewUiModel): ScreenStateEvent()
    data class SetTotalLikes(val totalLikes: String): ScreenStateEvent()
    data class SetPinned(val pinnedMessage: PinnedMessageUiModel) : ScreenStateEvent()
    data class SetQuickReply(val quickReply: QuickReplyUiModel) : ScreenStateEvent()
    data class IncomingChat(val chat: PlayChat) : ScreenStateEvent()
    object ComposeChat : ScreenStateEvent()
    object ShowOneTapOnboarding : ScreenStateEvent()
    data class VideoPropertyChanged(val videoProp: VideoPropertyUiModel) : ScreenStateEvent()
    data class VideoStreamChanged(val videoStream: VideoStreamUiModel) : ScreenStateEvent()
}