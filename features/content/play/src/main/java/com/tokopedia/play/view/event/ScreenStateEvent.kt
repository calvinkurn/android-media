package com.tokopedia.play.view.event

import com.google.android.exoplayer2.ExoPlayer
import com.tokopedia.play.component.ComponentEvent
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.uimodel.*

/**
 * Created by jegul on 02/12/19
 */
sealed class ScreenStateEvent : ComponentEvent {

    object Init : ScreenStateEvent()

    /**
     * Setter
     */
    data class SetVideo(val videoPlayer: ExoPlayer) : ScreenStateEvent()
    data class SetChannelTitle(val title: String): ScreenStateEvent()
    data class SetPartnerInfo(val partnerInfo: PartnerInfoUiModel): ScreenStateEvent()
    data class SetTotalCart(val cartUiModel: CartUiModel): ScreenStateEvent()
    data class SetTotalViews(val totalView: TotalViewUiModel): ScreenStateEvent()
    data class SetTotalLikes(val totalLikes: TotalLikeUiModel): ScreenStateEvent()
    data class SetPinned(val pinned: PinnedUiModel) : ScreenStateEvent()
    data class SetQuickReply(val quickReply: QuickReplyUiModel) : ScreenStateEvent()
    data class SetProductSheet(val productSheetModel: ProductSheetUiModel) : ScreenStateEvent()
    data class SetVariantSheet(val variantSheetModel: VariantSheetUiModel) : ScreenStateEvent()
    /**
     * Chat
     */
    data class IncomingChat(val chat: PlayChatUiModel) : ScreenStateEvent()
    object ComposeChat : ScreenStateEvent()
    /**
     * Like
     */
    data class LikeContent(val shouldLike: Boolean, val animate: Boolean) : ScreenStateEvent()
    /**
     * Follow
     */
    data class FollowPartner(val shouldFollow: Boolean) : ScreenStateEvent()
    /**
     * Keyboard
     */
    data class BottomInsetsChanged(val insetsViewMap: Map<BottomInsetsType, BottomInsetsState>, val isAnyShown: Boolean, val isAnyHidden: Boolean) : ScreenStateEvent()

    @Deprecated(
            message = "Use BottomInsetsView with type BottomInsetsType.Keyboard",
            replaceWith = ReplaceWith("BottomInsetsView(BottomInsetsType.Keyboard, isShown)", "com.tokopedia.play.view.event.ScreenStateEvent", "com.tokopedia.play.view.type.BottomInsetsType"),
            level = DeprecationLevel.WARNING
    )
    data class KeyboardStateChanged(val isShown: Boolean) : ScreenStateEvent()
    /**
     * Video
     */
    data class VideoPropertyChanged(val videoProp: VideoPropertyUiModel) : ScreenStateEvent()
    data class VideoStreamChanged(val videoStream: VideoStreamUiModel) : ScreenStateEvent()
    /**
     * Room Event
     */
    data class OnNewPlayRoomEvent(val event: PlayRoomEvent) : ScreenStateEvent()

    object OnNoMoreAction : ScreenStateEvent()
    object ShowOneTapOnboarding : ScreenStateEvent()
}